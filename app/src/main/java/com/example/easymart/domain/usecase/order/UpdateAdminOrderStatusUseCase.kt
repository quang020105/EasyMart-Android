package com.example.easymart.domain.usecase.order

import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.domain.repository.OrderRepository
import javax.inject.Inject

class UpdateAdminOrderStatusUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(
        remoteId: String,
        orderStatus: OrderStatus,
        paymentStatus: PaymentStatus? = null
    ) {
        orderRepository.updateOrderStatusForAdmin(remoteId, orderStatus, paymentStatus)
    }
}
