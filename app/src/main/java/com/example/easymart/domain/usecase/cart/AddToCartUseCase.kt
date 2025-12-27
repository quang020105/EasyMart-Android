package com.example.easymart.domain.usecase.cart

import com.example.easymart.domain.model.CartItem
import com.example.easymart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(cardItem: CartItem){
        cartRepository.addCartItem(cardItem)
    }
}