package com.example.easymart.domain.repository

import com.example.easymart.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun observeCartItems(userId: String?): Flow<List<CartItem>>
    suspend fun addToCart(userId: String?, cartItem: CartItem)
    suspend fun mergeGuestCartIntoUser(userId: String)
    suspend fun syncCart(userId: String)
    //suspend fun clearAllCartItems()
    suspend fun updateQuantity(cartItem: CartItem, delta: Int)
}