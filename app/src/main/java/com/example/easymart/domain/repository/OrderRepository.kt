package com.example.easymart.domain.repository

import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderItem
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getObserveAllOrders(userId: String): Flow<List<Order>>
    suspend fun getOrderItemById(orderItemId: Int): OrderItem?
    suspend fun getOrderById(orderId: Int): Order?
    suspend fun saveOrderLocally(order: Order): Int
    suspend fun syncOrder(orderId: Int)
    suspend fun syncPendingOrders(userId: String)
    suspend fun pullRemoteOrders(userId: String)
    fun observeRemoteOrders(userId: String): Flow<Unit>
}
