package com.example.easymart.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.easymart.data.local.entity.OrderEntity
import com.example.easymart.data.local.entity.OrderItemEntity
import com.example.easymart.data.local.relation.OrderWithItems
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.domain.model.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Transaction
    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrderWithItems(orderId: Int): OrderWithItems

    @Transaction
    @Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
    suspend fun getOrderWithItemsOrNull(orderId: Int): OrderWithItems?

    @Transaction
    @Query("SELECT * FROM orders where userId = :userId order by createdAt desc")
    fun getObserveAllOrdersWithItems(userId: String): Flow<List<OrderWithItems>>

    @Query("SELECT * FROM order_items WHERE id = :orderItemId")
    suspend fun getOrderItemById(orderItemId: Int): OrderItemEntity?

    @Transaction
    @Query("SELECT * FROM orders WHERE userId = :userId AND isSynced = 0 ORDER BY updatedAt ASC")
    suspend fun getUnsyncedOrdersWithItems(userId: String): List<OrderWithItems>

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


    // server
    @Query("UPDATE orders SET serverOrderId = :serverOrderId WHERE id = :localId")
    suspend fun updateServerOrderId(localId: Int, serverOrderId: Int)

    @Query(
        """
        UPDATE orders
        SET remoteId = :remoteId,
            isSynced = :isSynced,
            syncStatus = :syncStatus,
            updatedAt = :updatedAt
        WHERE id = :orderId
        """
    )
    suspend fun updateOrderSyncState(
        orderId: Int,
        remoteId: String?,
        isSynced: Boolean,
        syncStatus: SyncStatus,
        updatedAt: Long
    )

    @Query("UPDATE orders SET paymentStatus = :status WHERE serverOrderId = :serverOrderId")
    suspend fun updatePaymentStatusByServerId(serverOrderId: Int, status: String)

    @Query("SELECT * FROM orders WHERE serverOrderId = :serverOrderId LIMIT 1")
    suspend fun getByServerOrderId(serverOrderId: Int): OrderEntity?

    @Query("SELECT * FROM orders WHERE id = :localId LIMIT 1")
    suspend fun getByLocalId(localId: Int): OrderEntity?
}
