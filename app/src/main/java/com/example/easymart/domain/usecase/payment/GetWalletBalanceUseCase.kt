package com.example.easymart.domain.usecase.payment

import com.example.easymart.domain.repository.PaymentRepository
import javax.inject.Inject

class GetWalletBalanceUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(userId: String): Long {
        return paymentRepository.getWalletBalance(userId = userId)
    }
}