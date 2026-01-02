package com.example.easymart.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.easymart.data.local.entity.OrderEntity
import com.example.easymart.data.local.entity.OrderItemEntity
import com.example.easymart.data.local.entity.OrderWithItems

@Dao
interface OrderDao {
    @Transaction
    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrderWithItems(orderId: Int): OrderWithItems

    @Transaction
    @Query("SELECT * FROM orders where userId = :userId order by createdAt desc")
    suspend fun getAllOrdersWithItems(userId: Int): List<OrderWithItems>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Insert
    suspend fun insertOrderItems(orderItems: List<OrderItemEntity>)

    @Transaction
    suspend fun insertOrderWithItems(
        order: OrderEntity,
        orderItems: List<OrderItemEntity>
    ) {
        insertOrder(order)
        insertOrderItems(orderItems)
    }
}