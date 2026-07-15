package com.example.easymart.domain.usecase.cart

import com.example.easymart.domain.model.CartItem
import com.example.easymart.domain.repository.CartRepository
import javax.inject.Inject

class RemovePurchasedCartItemsUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(cartItems: List<CartItem>) {
        cartRepository.removePurchasedCartItems(cartItems)
    }
}
