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
    suspend fun requestCancellation(
        remoteId: String,
        requesterId: String,
        reason: String,
        updatedAt: Long
    ): OrderRemoteDto
    suspend fun cancelCreatedOrder(
        remoteId: String,
        requesterId: String,
        reason: String,
        updatedAt: Long
    ): OrderRemoteDto
    suspend fun approveCancellation(
        remoteId: String,
        adminId: String,
        updatedAt: Long
    ): OrderRemoteDto
    suspend fun confirmManualRefund(
        remoteId: String,
        adminId: String,
        updatedAt: Long
    ): OrderRemoteDto
}
