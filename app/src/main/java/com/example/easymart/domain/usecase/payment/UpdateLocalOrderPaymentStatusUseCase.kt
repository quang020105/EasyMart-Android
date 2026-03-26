package com.example.easymart.domain.usecase.payment

import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.domain.repository.PaymentRepository
import javax.inject.Inject

class UpdateLocalOrderPaymentStatusUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(localOrderId: Int, status: PaymentStatus) {
        paymentRepository.updateLocalOrderPaymentStatus(localOrderId, status)
    }
}

