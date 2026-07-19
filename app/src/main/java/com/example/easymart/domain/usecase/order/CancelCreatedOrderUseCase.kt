package com.example.easymart.domain.usecase.order

import com.example.easymart.domain.repository.OrderRepository
import javax.inject.Inject

class CancelCreatedOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(orderId: Int, requesterId: String, reason: String) {
        orderRepository.cancelCreatedOrder(orderId, requesterId, reason)
    }
}
