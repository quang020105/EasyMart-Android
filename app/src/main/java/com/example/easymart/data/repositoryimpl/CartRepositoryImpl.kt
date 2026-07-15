package com.example.easymart.data.repositoryimpl

import android.util.Log
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val localDS: CartLocalDataSource,
    private val remoteDS: CartRemoteDataSource
) : CartRepository {

    private val syncMutex = Mutex()

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun observeCartItems(userId: String?): Flow<List<CartItem>> {
        val cart = localDS.getOrCreateCart(userId)
        return localDS.observeCartItems(cart.id).map { entities ->
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
            val existing = localDS.getExistingByProductId(cart.id, cartItem.product.id)
            if (existing != null) {
                val newQuantity = existing.quantity + cartItem.quantity
                val updated = existing.copy(
                    quantity = newQuantity,
                    isSynced = false,
                    isDeleted = false,
                    updatedAt = now
                )
                localDS.updateCartItem(updated)
//                if (userId != null) {
//                    remoteDS.upsertCartItem(userId, updated.toRemoteDto())
//                }
                Log.d("CartRepositoryImpl", "updateCartItem: ${cartItem.toEntity(cart.id)}")
            } else {
                localDS.insertCartItem(cartItem.toEntity(cart.id).copy(
                    isSynced = false,
                    isDeleted = false,
                    updatedAt = now
                ))
//                if (userId != null) {
//                    val inserted = localDS.getExistingByProductId(cart.id, cartItem.product.id)
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
            val guestCart = localDS.getGuestCart() ?: return@withContext // nếu không có giỏ hàng thì return
            val userCart = localDS.getOrCreateCart(userId) //lấy hoặc tạo giỏ hàng cho user

            val guestItems = localDS.getAllCartItemsOnce(guestCart.id)
            val userItems = localDS.getAllCartItemsOnce(userCart.id)

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
            localDS.clearAllCartItems(userCart.id)
            mergedItems.forEach { item -> localDS.insertCartItem(item) }

            //xóa giỏ hàng của guest sau khi đã gộp
            localDS.clearAllCartItems(guestCart.id)
            localDS.deleteCart(guestCart)

            remoteDS.replaceAllCartItems(userId, mergedItems.map { it.toRemoteDto() })
        }
    }

    override suspend fun syncCart(userId: String) {
        withContext(Dispatchers.IO) {
            syncMutex.withLock {
            val cart = localDS.getOrCreateCart(userId)
            val unSyncedItems = localDS.getUnsyncedCartItems(cartId = cart.id)
            unSyncedItems.forEach { local ->
                runCatching {
                    if (local.isDeleted || local.quantity <= 0) {
                        remoteDS.deleteCartItem(userId, local.productId)
                        localDS.deleteCartItem(local)
                    } else {
                        remoteDS.upsertCartItem(userId, local.toRemoteDto())
                        localDS.markCartItemSynced(local.id)
                    }
                }.onFailure {
                    Log.e("CartRepositoryImpl", "Đồng bộ thất bại ${local.id}", it)
                }
            }
            val remoteItems = remoteDS.getCartItemsOnce(userId)
            syncRemoteToLocal(cart.id, remoteItems)
            }
        }
    }

    override suspend fun updateQuantity(cartItem: CartItem, delta: Int) {
        withContext(Dispatchers.IO) {
            val cartId = localDS.getCartIdByCartItemId(cartItem.id) ?: return@withContext
            localDS.getCartById(cartId) ?: return@withContext
            val existing = localDS.getExistingByProductId(cartId, cartItem.product.id) ?: return@withContext

            val newQuantity = existing.quantity + delta
            if (newQuantity <= 0) {
                localDS.markCartItemDeleted(existing.id, System.currentTimeMillis())
                return@withContext
            }

            localDS.updateCartItem(cartItem.toEntity(cartId).copy(
                quantity = newQuantity,
                isSynced = false,
                isDeleted = false,
                updatedAt = System.currentTimeMillis()
            ))
        }
    }

    override suspend fun removeCartItem(cartItem: CartItem) {
        withContext(Dispatchers.IO){
            val cartId = localDS.getCartIdByCartItemId(cartItem.id) ?: return@withContext
            val existing = localDS.getExistingByProductId(cartId, cartItem.product.id) ?: return@withContext
            localDS.markCartItemDeleted(existing.id, System.currentTimeMillis())
        }
    }

    override suspend fun removePurchasedCartItems(cartItems: List<CartItem>) {
        withContext(Dispatchers.IO) {
            cartItems.forEach { cartItem ->
                val cartId = localDS.getCartIdByCartItemId(cartItem.id) ?: return@forEach
                val existing = localDS.getExistingByProductId(cartId, cartItem.product.id) ?: return@forEach
                localDS.markCartItemDeleted(existing.id, System.currentTimeMillis())
            }
        }
    }


    // Hàm này sẽ đồng bộ dữ liệu từ remote về local
    private suspend fun syncRemoteToLocal(cartId: String, remoteItems: List<CartItemRemoteDto>) {
        withContext(Dispatchers.IO) {
            // lọc ra các item remote mới nhất theo productId (nếu có nhiều bản ghi cùng productId)
            val normalizedRemoteItems = remoteItems
                .groupBy { it.productId }
                .mapNotNull { (_, items) -> items.maxByOrNull { it.updatedAt } }

            val localItems = localDS.getAllCartItemsIncludingDeleted(cartId)
            val localByProductId = localItems.associateBy { it.productId }
            val remoteByProductId = normalizedRemoteItems.associateBy { it.productId }

            // Cập nhật/insert theo remote
            normalizedRemoteItems.forEach { remote ->
                val local = localByProductId[remote.productId]
                if (local == null) {
                    localDS.upsertCartItemByProduct(remote.toEntity(cartId).copy(isSynced = true))
                } else {
                    if (local.isDeleted) {
                        // Local đã xoá, ưu tiên xoá (không hồi sinh từ remote)
                        return@forEach
                    }
                    val localHasPendingChange = !local.isSynced
                    val remoteIsNewer = remote.updatedAt > local.updatedAt
                    when {
                        localHasPendingChange && !remoteIsNewer -> {
                            // Local có thay đổi chưa đồng bộ nhưng remote cũ hơn, ưu tiên local
                            return@forEach
                        }
                        remoteIsNewer -> {
                            // Remote mới hơn, ưu tiên remote
                            localDS.updateCartItem(local.copy(
                                name = remote.name,
                                price = remote.price,
                                imageUrl = remote.imageUrl,
                                quantity = remote.quantity,
                                updatedAt = remote.updatedAt,
                                addAt = remote.addAt,
                                isSynced = true,
                                isDeleted = false
                            ))
                        }
                    }
                }
            }

            // Xóa local item nếu remote không còn (chỉ khi local không có pending change)
            localItems.forEach { local ->
                val remote = remoteByProductId[local.productId]
                if (remote == null && local.isSynced) {
                    localDS.deleteCartItem(local)
                }
            }
        }
    }
}
