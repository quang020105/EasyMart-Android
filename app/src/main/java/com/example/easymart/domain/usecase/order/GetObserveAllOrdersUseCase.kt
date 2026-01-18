package com.example.easymart.domain.usecase.order

import com.example.easymart.domain.repository.OrderRepository
import javax.inject.Inject

class GetObserveAllOrdersUseCase @Inject constructor (
    private val orderRepository: OrderRepository
){
    operator fun invoke(userId: String) = orderRepository.getObserveAllOrders(userId)
}