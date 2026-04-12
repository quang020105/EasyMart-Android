package com.example.easymart.domain.usecase.cart

import com.example.easymart.domain.repository.CartRepository
import javax.inject.Inject

class SyncCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(userId: String) {
        cartRepository.syncCart(userId)
    }
}