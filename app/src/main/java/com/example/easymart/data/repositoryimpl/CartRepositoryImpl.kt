package com.example.easymart.data.repositoryimpl

import android.util.Log
import com.example.easymart.data.local.dao.CartDao
import com.example.easymart.data.local.datasource.CartLocalDataSource
import com.example.easymart.data.mapper.toDomain
import com.example.easymart.data.mapper.toEntity
import com.example.easymart.data.mapper.toRemoteDto
import com.example.easymart.data.remote.datasource.CartRemoteDataSource
import com.example.easymart.data.remote.dto.CartItemRemoteDto
import com.example.easymart.domain.model.CartItem
import com.example.easymart.domain.repository.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao,
    private val localDS: CartLocalDataSource,
    private val remoteDS: CartRemoteDataSource
) : CartRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun observeCartItems(userId: String?): Flow<List<CartItem>> {
        val cart = localDS.getOrCreateCart(userId)
        return cartDao.getAllCartItems(cart.id).map { entities ->
            entities.map { it.toDomain() }
        }

        //if (userId == null) return localFlow

//        return remoteDS.observeCartItems(userId)
//            .catch { e ->
//                Log.e("CartRepositoryImpl", "Remote cart listen failed", e)
//                emit(emptyList())
//            }
//            .flatMapLatest { remoteItems ->
//                syncRemoteToLocal(cart.id, remoteItems)
//                localFlow
//            }
    }

    override suspend fun addToCart(
        userId: String?,
        cartItem: CartItem
    ) {
        withContext(Dispatchers.IO) {
            val cart = localDS.getOrCreateCart(userId)
            val now = System.currentTimeMillis()
            val existing = cartDao.getExistingByProductId(cart.id, cartItem.product.id)
            if (existing != null) {
                val newQuantity = existing.quantity + cartItem.quantity
                val updated = existing.copy(quantity = newQuantity, isSynced = false, updatedAt = now)
                cartDao.updateCartItem(updated)
//                if (userId != null) {
//                    remoteDS.upsertCartItem(userId, updated.toRemoteDto())
//                }
                Log.d("CartRepositoryImpl", "updateCartItem: ${cartItem.toEntity(cart.id)}")
            } else {
                cartDao.insertCartItem(cartItem.toEntity(cart.id).copy(
                    isSynced = false,
                    updatedAt = now
                ))
//                if (userId != null) {
//                    val inserted = cartDao.getExistingByProductId(cart.id, cartItem.product.id)
//                    if (inserted != null) {
//                        remoteDS.upsertCartItem(userId, inserted.toRemoteDto())
//                    }
//                }
                Log.d("CartRepositoryImpl", "insertCartItem: ${cartItem.toEntity(cart.id)}")
            }
        }
    }

    override suspend fun mergeGuestCartIntoUser(userId: String) {
        withContext(Dispatchers.IO) {
            val guestCart = cartDao.getGuestCart() ?: return@withContext // nếu không có giỏ hàng thì return
            val userCart = localDS.getOrCreateCart(userId) //lấy hoặc tạo giỏ hàng cho user

            val guestItems = cartDao.getAllCartItemsOnce(guestCart.id)
            val userItems = cartDao.getAllCartItemsOnce(userCart.id)

            //trả về danh sách sản phẩm được gộp dựa vào productId
            val mergedItems = (guestItems + userItems)
                .groupBy { it.productId }
                .map { (_, items) ->
                    val first = items.first()
                    first.copy(
                        id = 0, // mặc định 0 để Room tự sinh id mới
                        cartId = userCart.id,
                        quantity = items.sumOf { it.quantity },
                        isSynced = false,
                        updatedAt = System.currentTimeMillis()
                    )
                }

            //thay thế danh sách sản phẩm trong giỏ hàng của user bằng danh sách đã gộp
            cartDao.clearAllCartItems(userCart.id)
            mergedItems.forEach { item -> cartDao.insertCartItem(item) }

            //xóa giỏ hàng của guest sau khi đã gộp
            cartDao.clearAllCartItems(guestCart.id)
            cartDao.deleteCart(guestCart)

            remoteDS.replaceAllCartItems(userId, mergedItems.map { it.toRemoteDto() })
        }
    }

    override suspend fun syncCart(userId: String) {
        withContext(Dispatchers.IO) {
            val cart = localDS.getOrCreateCart(userId)
            val unSyncedItems = cartDao.getUnsyncedCartItems(cartId = cart.id)
            unSyncedItems.forEach { local ->
                runCatching {
                    if(local.quantity <= 0){
                        cartDao.deleteCartItem(local)
                        remoteDS.deleteCartItem(userId, local.productId)
                    } else {
                        remoteDS.upsertCartItem(userId, local.toRemoteDto())
                    }
                }.onFailure {
                    Log.e("CartRepositoryImpl", "Đồng bộ thất bại ${local.id}", it)
                }
            }
            val remoteItems = remoteDS.getCartItemsOnce(userId)
            syncRemoteToLocal(cart.id, remoteItems)
        }
    }

    override suspend fun updateQuantity(cartItem: CartItem, delta: Int) {
        withContext(Dispatchers.IO) {
            //cartDao.updateQuantityById(cartItem.id, delta)
            val cartId = cartDao.getCartIdByCartItemId(cartItem.id) ?: return@withContext
            val cart = cartDao.getCartById(cartId) ?: return@withContext
            val userId = cart.userId ?: return@withContext
            val existing = cartDao.getExistingByProductId(cartId, cartItem.product.id) ?: return@withContext
//            if (updated != null) {
//                if (updated.quantity <= 0) {
//                    remoteDS.deleteCartItem(userId, updated.productId)
//                } else {
//                    remoteDS.upsertCartItem(userId, updated.toRemoteDto())
//                }
//            }

           val newQuantity = existing.quantity + delta
            if (newQuantity <= 0) {
                cartDao.deleteCartItem(existing)
                return@withContext
            }

            cartDao.updateCartItem(cartItem.toEntity(cartId).copy(
                quantity = newQuantity,
                isSynced = false,
                updatedAt = System.currentTimeMillis()
            ))
        }
    }


    // Hàm này sẽ đồng bộ dữ liệu từ remote về local
    private suspend fun syncRemoteToLocal(cartId: String, remoteItems: List<CartItemRemoteDto>) {
        withContext(Dispatchers.IO) {
            val localItems = cartDao.getAllCartItemsOnce(cartId)
            val localByProductId = localItems.associateBy { it.productId }
            val remoteByProductId = remoteItems.associateBy { it.productId }

            // Cập nhật/insert theo remote
            remoteItems.forEach { remote ->
                val local = localByProductId[remote.productId]
                if (local == null) {
                    cartDao.insertCartItem(remote.toEntity(cartId).copy(isSynced = true))
                } else {
                    val localHasPendingChange = !local.isSynced
                    val remoteIsNewer = remote.updatedAt > local.updatedAt
                    when {
                        localHasPendingChange && !remoteIsNewer -> {
                            // Local có thay đổi chưa đồng bộ nhưng remote cũ hơn, ưu tiên local
                            return@forEach
                        }
                        remoteIsNewer -> {
                            // Remote mới hơn, ưu tiên remote
                            cartDao.updateCartItem(local.copy(
                                name = remote.name,
                                price = remote.price,
                                imageUrl = remote.imageUrl,
                                quantity = remote.quantity,
                                updatedAt = remote.updatedAt,
                                addAt = remote.addAt,
                                isSynced = true
                            ))
                        }
                    }
                }
            }

            // Xóa local item nếu remote không còn
            localItems.forEach { local ->
                val remote = remoteByProductId[local.productId]
                if(remote == null && local.isSynced){
                    cartDao.deleteCartItem(local)
                }
            }
        }
    }
}