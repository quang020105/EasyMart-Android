package com.example.easymart.domain.repository

import com.example.easymart.data.local.dao.OrderDao
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface OrderRepository {
    fun getObserveAllOrders(userId: String): Flow<List<Order>>
    suspend fun getOrderById(orderItemId: Int): OrderItem?
}