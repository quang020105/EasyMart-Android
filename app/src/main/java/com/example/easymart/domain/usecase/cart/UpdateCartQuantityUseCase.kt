package com.example.easymart.domain.usecase.cart

import com.example.easymart.domain.model.CartItem
import com.example.easymart.domain.repository.CartRepository
import javax.inject.Inject

class UpdateCartQuantityUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(cartItem: CartItem, delta: Int){
        cartRepository.updateQuantity(cartItem, delta)
    }
}