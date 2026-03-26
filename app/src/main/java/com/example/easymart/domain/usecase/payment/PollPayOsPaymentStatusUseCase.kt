package com.example.easymart.domain.usecase.payment

import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.domain.repository.PaymentRepository
import javax.inject.Inject

class PollPayOsPaymentStatusUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(
        orderCode: Long,
        maxRetries: Int = 20,
        delayMs: Long = 2000L
    ): PaymentStatus {
        return paymentRepository.pollPayOsUntilDone(orderCode, maxRetries, delayMs)
    }
}

