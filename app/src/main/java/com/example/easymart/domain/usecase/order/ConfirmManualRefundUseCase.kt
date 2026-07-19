package com.example.easymart.domain.usecase.order

import com.example.easymart.domain.repository.OrderRepository
import javax.inject.Inject

class ConfirmManualRefundUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(remoteId: String, adminId: String) {
        orderRepository.confirmManualRefund(remoteId, adminId)
    }
}
