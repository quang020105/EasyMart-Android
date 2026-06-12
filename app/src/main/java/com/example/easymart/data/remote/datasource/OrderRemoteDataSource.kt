package com.example.easymart.data.remote.datasource

import com.example.easymart.data.remote.dto.OrderRemoteDto

interface OrderRemoteDataSource {
    suspend fun upsertOrder(userId: String, order: OrderRemoteDto): String
}
