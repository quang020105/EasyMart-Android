package com.example.easymart.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.easymart.data.local.entity.OrderEntity
import com.example.easymart.data.local.entity.OrderItemEntity
import com.example.easymart.data.local.entity.OrderWithItems
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentStatus

@Dao
interface OrderDao {
    @Transaction
    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrderWithItems(orderId: Int): OrderWithItems

    @Transaction
    @Query("SELECT * FROM orders where userId = :userId order by createdAt desc")
    suspend fun getAllOrdersWithItems(userId: Int): List<OrderWithItems>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity): Long

    @Insert
    suspend fun insertOrderItems(orderItems: List<OrderItemEntity>)

    @Transaction
    suspend fun insertOrderWithItems(
        order: OrderEntity,
        orderItems: List<OrderItemEntity>
    ): Int {
        val orderId = insertOrder(order).toInt()
        val itemsWithOrderId = orderItems.map { it.copy(orderId = orderId) }
        insertOrderItems(itemsWithOrderId)
        return orderId
    }

    //dùng khi chỉ cập nhật trạng thái đơn hàng
    @Query("UPDATE orders SET orderStatus = :status WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Int, status: OrderStatus)

    //dùng khi cập nhật cả trạng thái đơn hàng và trạng thái thanh toán(khi xử lý thanh toán)
    @Query("UPDATE orders set orderStatus = :status, paymentStatus = :paymentStatus WHERE id = :orderId")
    suspend fun updateOrderAndPaymentStatus(orderId: Int, status: OrderStatus, paymentStatus: PaymentStatus)
}