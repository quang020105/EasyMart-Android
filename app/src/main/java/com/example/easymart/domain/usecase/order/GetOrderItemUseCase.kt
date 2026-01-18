package com.example.easymart.domain.usecase.order

import com.example.easymart.domain.repository.OrderRepository
import javax.inject.Inject

class GetOrderItemUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(orderItemId: Int) =
        orderRepository.getOrderById(orderItemId)
}