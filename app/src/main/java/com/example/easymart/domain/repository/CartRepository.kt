package com.example.easymart.domain.repository

import androidx.compose.ui.graphics.FilterQuality
import com.example.easymart.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getAllCartItems(): Flow<List<CartItem>>
    suspend fun addCartItem(cartItem: CartItem)
    suspend fun clearAllCartItems()
    suspend fun updateQuantity(cartItem: CartItem, delta: Int)
}