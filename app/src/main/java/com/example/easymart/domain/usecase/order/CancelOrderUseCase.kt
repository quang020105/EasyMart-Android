package com.example.easymart.domain.usecase.order

import com.example.easymart.data.local.dao.OrderDao
import com.example.easymart.domain.model.OrderStatus
import javax.inject.Inject

class CancelOrderUseCase @Inject constructor(
    private val orderDao: OrderDao,
    private val orderAutoProcessUseCase: OrderAutoProcessUseCase
) {
    suspend operator fun invoke(orderId: Int) {
        orderDao.updateOrderStatus(orderId, OrderStatus.CANCELLED)
        orderAutoProcessUseCase.cancel(orderId)
    }
}