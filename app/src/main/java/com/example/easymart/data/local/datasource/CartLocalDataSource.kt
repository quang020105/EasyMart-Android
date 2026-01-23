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
}