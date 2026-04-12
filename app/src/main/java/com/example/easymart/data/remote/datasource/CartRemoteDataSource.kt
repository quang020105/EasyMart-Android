package com.example.easymart.data.remote.datasource

import com.example.easymart.data.remote.dto.CartItemRemoteDto
import kotlinx.coroutines.flow.Flow

interface CartRemoteDataSource {
    fun observeCartItems(userId: String): Flow<List<CartItemRemoteDto>>
    suspend fun upsertCartItem(userId: String, item: CartItemRemoteDto)
    suspend fun deleteCartItem(userId: String, productId: Int)
    suspend fun getCartItemsOnce(userId: String): List<CartItemRemoteDto>
    suspend fun replaceAllCartItems(userId: String, items: List<CartItemRemoteDto>)
    suspend fun clearCart(userId: String)
}

