package com.example.easymart.data.repositoryimpl

import com.example.easymart.data.local.dao.OrderDao
import com.example.easymart.data.mapper.toDomain
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderItem
import com.example.easymart.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao
) : OrderRepository {
    override fun getObserveAllOrders(userId: String): Flow<List<Order>> =
        orderDao.getObserveAllOrdersWithItems(userId).map { list -> list.map { it.toDomain() } }


    override suspend fun getOrderItemById(orderItemId: Int): OrderItem? =
        orderDao.getOrderItemById(orderItemId)?.toDomain()

    override suspend fun getOrderById(orderId: Int): Order? {
        orderDao.getOrderWithItems(orderId).let {
            return it.toDomain()
        }
    }
}