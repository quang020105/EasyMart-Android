package com.example.easymart.domain.repository

import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderItem
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getObserveAllOrders(userId: String): Flow<List<Order>>
    suspend fun getOrderItemById(orderItemId: Int): OrderItem?
    suspend fun getOrderById(orderId: Int): Order?
}