package com.example.easymart.data.remote.datasource

import com.example.easymart.data.remote.dto.OrderRemoteDto
import kotlinx.coroutines.flow.Flow

interface OrderRemoteDataSource {
    suspend fun upsertOrder(userId: String, order: OrderRemoteDto): String
    suspend fun getOrdersOnce(userId: String): List<OrderRemoteDto>
    fun observeOrders(userId: String): Flow<List<OrderRemoteDto>>
    fun observeAllOrders(): Flow<List<OrderRemoteDto>>
    suspend fun getOrderByRemoteId(remoteId: String): OrderRemoteDto?
    suspend fun updateOrderStatus(
        remoteId: String,
        orderStatus: String,
        paymentStatus: String?,
        updatedAt: Long
    )
}
