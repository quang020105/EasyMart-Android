package com.example.easymart.data.repositoryimpl

import android.util.Log
import com.example.easymart.data.local.dao.CartDao
import com.example.easymart.data.local.datasource.CartLocalDataSource
import com.example.easymart.data.mapper.toDomain
import com.example.easymart.data.mapper.toEntity
import com.example.easymart.domain.model.CartItem
import com.example.easymart.domain.repository.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao,
    private val localDS: CartLocalDataSource
) : CartRepository {
    override suspend fun observeCartItems(userId: String?): Flow<List<CartItem>> {
        val cart = localDS.getOrCreateCart(userId)
        return cartDao.getAllCartItems(cart.id).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addToCart(
        userId: String?,
        cartItem: CartItem
    ) {
        withContext(Dispatchers.IO) {
            val cart = localDS.getOrCreateCart(userId)
            val existing = cartDao.getExistingByProductId(cart.id, cartItem.product.id)
            if (existing != null) {
                val newQuantity = existing.quantity + cartItem.quantity
                cartDao.updateCartItem(existing.copy(quantity = newQuantity))
                Log.d("CartRepositoryImpl", "updateCartItem: ${cartItem.toEntity(cart.id)}")
            } else {
                cartDao.insertCartItem(cartItem.toEntity(cart.id))
                Log.d("CartRepositoryImpl", "insertCartItem: ${cartItem.toEntity(cart.id)}")
            }
        }
    }

    override suspend fun mergeGuestCartIntoUser(userId: String) {
        withContext(Dispatchers.IO) {
            val guestCart = cartDao.getGuestCart() ?: return@withContext // nếu không có giỏ hàng thì return
            val userCart = localDS.getOrCreateCart(userId) //lấy hoặc tạo giỏ hàng cho user

            //lấy danh sách các sản phẩm
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
                        quantity = items.sumOf { it.quantity }
                    )
                }

            //thay thế danh sách sản phẩm trong giỏ hàng của user bằng danh sách đã gộp
            cartDao.clearAllCartItems(userCart.id)
            mergedItems.forEach { item -> cartDao.insertCartItem(item) }

            //xóa giỏ hàng của guest sau khi đã gộp
            cartDao.clearAllCartItems(guestCart.id)
            cartDao.deleteCart(guestCart)
        }
    }

//    override suspend fun addToCart(cartItem: CartItem) {
//        withContext(Dispatchers.IO){
//            val existing = cartDao.getExistingByProductId(cartItem.product.id)
//            if(existing != null){
//                val newQuantity = existing.quantity + cartItem.quantity
//                cartDao.updateCartItem(existing.copy(quantity = newQuantity))
//            } else {
//                cartDao.insertCartItem(cartItem.toEntity())
//            }
//        }
//    }

//    override suspend fun clearAllCartItems() {
//        //withContext(Dispatchers.IO) {
//        //}
//
//        cartDao.clearAllCartItems()
//    }

    override suspend fun updateQuantity(cartItem: CartItem, delta: Int) {
        withContext(Dispatchers.IO) {
            cartDao.updateQuantityById(cartItem.id, delta)
        }
    }
}