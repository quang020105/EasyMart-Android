package com.example.easymart.data.local.datasource

import com.example.easymart.data.local.dao.CartDao
import com.example.easymart.data.local.entity.CartEntity
import com.example.easymart.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class CartLocalDataSource @Inject constructor(
    private val cartDao: CartDao
){
    //lấy giỏ hàng của user hoặc guest nếu đã tồn tại giỏ hàng ,nếu chưa thì tạo mới
    suspend fun getOrCreateCart(userId: String?): CartEntity {
        val existing = if (userId == null)
            cartDao.getGuestCart()
        else
            cartDao.getUserCart(userId)

        if (existing != null) return existing

        val cart = CartEntity(
            id = UUID.randomUUID().toString(),
            userId = userId,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        cartDao.insertCart(cart)
        return cart
    }

    fun observeCartItems(cartId: String): Flow<List<CartItemEntity>> {
        return cartDao.getAllCartItems(cartId)
    }

    suspend fun getGuestCart(): CartEntity? = cartDao.getGuestCart()

    suspend fun getCartById(cartId: String): CartEntity? = cartDao.getCartById(cartId)

    suspend fun deleteCart(cart: CartEntity) = cartDao.deleteCart(cart)

    suspend fun getExistingByProductId(cartId: String, productId: Int): CartItemEntity? =
        cartDao.getExistingByProductId(cartId, productId)

    suspend fun getAllCartItemsOnce(cartId: String): List<CartItemEntity> =
        cartDao.getAllCartItemsOnce(cartId)

    suspend fun getAllCartItemsIncludingDeleted(cartId: String): List<CartItemEntity> =
        cartDao.getAllCartItemsIncludingDeleted(cartId)

    suspend fun getUnsyncedCartItems(cartId: String): List<CartItemEntity> =
        cartDao.getUnsyncedCartItems(cartId)

    suspend fun getCartIdByCartItemId(cartItemId: Int): String? =
        cartDao.getCartIdByCartItemId(cartItemId)

    suspend fun insertCartItem(cartItem: CartItemEntity) = cartDao.insertCartItem(cartItem)

    suspend fun updateCartItem(cartItem: CartItemEntity) = cartDao.updateCartItem(cartItem)

    suspend fun upsertCartItemByProduct(cartItem: CartItemEntity) =
        cartDao.upsertCartItemByProduct(cartItem)

    suspend fun deleteCartItem(cartItem: CartItemEntity) = cartDao.deleteCartItem(cartItem)

    suspend fun clearAllCartItems(cartId: String) = cartDao.clearAllCartItems(cartId)

    suspend fun markCartItemDeleted(id: Int, updatedAt: Long) =
        cartDao.markCartItemDeleted(id, updatedAt)

    suspend fun markCartItemSynced(id: Int) = cartDao.markCartItemSynced(id)
}
