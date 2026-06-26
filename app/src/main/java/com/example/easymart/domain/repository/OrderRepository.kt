package com.example.easymart.domain.repository

import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderItem
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentStatus
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getObserveAllOrders(userId: String): Flow<List<Order>>
    fun observeOrderById(orderId: Int): Flow<Order?>
    suspend fun getOrderItemById(orderItemId: Int): OrderItem?
    suspend fun getOrderById(orderId: Int): Order?
    suspend fun saveOrderLocally(order: Order): Int
    suspend fun syncOrder(orderId: Int)
    suspend fun syncPendingOrders(userId: String)
    suspend fun pullRemoteOrders(userId: String)
    fun observeRemoteOrders(userId: String): Flow<Unit>
    fun observeAllOrdersForAdmin(): Flow<List<Order>>
    suspend fun getOrderByRemoteId(remoteId: String): Order?
    suspend fun updateOrderStatusForAdmin(
        remoteId: String,
        orderStatus: OrderStatus,
        paymentStatus: PaymentStatus? = null
    )
}
