package com.example.easymart.domain.usecase.cart

import com.example.easymart.domain.model.CartItem
import com.example.easymart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCartItemsUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    operator fun invoke(): Flow<List<CartItem>> = cartRepository.getAllCartItems()
}