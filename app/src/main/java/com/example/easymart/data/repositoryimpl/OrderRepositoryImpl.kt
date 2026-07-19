package com.example.easymart.data.repositoryimpl

import android.util.Log
import com.example.easymart.data.local.dao.OrderDao
import com.example.easymart.data.mapper.toDomain
import com.example.easymart.data.mapper.toDomainOrder
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderItem
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.domain.model.SyncStatus
import com.example.easymart.domain.repository.OrderRepository
import com.example.easymart.data.mapper.toEntity
import com.example.easymart.data.mapper.toRemoteDto
import com.example.easymart.data.mapper.toVndNormalized
import com.example.easymart.data.remote.datasource.OrderRemoteDataSource
import com.example.easymart.data.remote.dto.OrderRemoteDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao,
    private val remoteDS: OrderRemoteDataSource
) : OrderRepository {
    override fun getObserveAllOrders(userId: String): Flow<List<Order>> =
        orderDao.getObserveAllOrdersWithItems(userId).map { list -> list.map { it.toDomain() } }

    override fun observeOrderById(orderId: Int): Flow<Order?> =
        orderDao.observeOrderWithItems(orderId).map { it?.toDomain() }

    override suspend fun getOrderItemById(orderItemId: Int): OrderItem? =
        orderDao.getOrderItemById(orderItemId)?.toDomain()

    override suspend fun getOrderById(orderId: Int): Order? {
        return orderDao.getOrderWithItemsOrNull(orderId)?.toDomain()
    }

    override suspend fun saveOrderLocally(order: Order): Int {
        val now = System.currentTimeMillis()
        return orderDao.insertOrderWithItems(
            order = order.copy(
                isSynced = false,
                syncStatus = SyncStatus.PENDING,
                createdAt = order.createdAt,
                updatedAt = now
            ).toEntity(),
            orderItems = order.items.map { it.toEntity() }
        )
    }

    // đồng bộ đơn hàng từ local lên remote
    override suspend fun syncOrder(orderId: Int) {
        val orderWithItems = orderDao.getOrderWithItemsOrNull(orderId) ?: return
        if (orderWithItems.order.isSynced && orderWithItems.order.syncStatus == SyncStatus.SYNCED) {
            return
        }

        val safeRemoteId = orderWithItems.order.remoteId ?: UUID.randomUUID().toString()
        val syncStartedAt = System.currentTimeMillis()

        orderDao.updateOrderSyncState(
            orderId = orderId,
            remoteId = safeRemoteId,
            isSynced = false,
            syncStatus = SyncStatus.SYNCING,
            updatedAt = syncStartedAt
        )

        runCatching {
            Log.d("OrderRepositoryImpl", "OrderWithItems to sync: ${orderWithItems.toDomain()}")
            val orderDto = orderWithItems.toRemoteDto().copy(
                remoteId = safeRemoteId,
                updatedAt = syncStartedAt
            )
            remoteDS.upsertOrder(
                userId = orderWithItems.order.userId,
                order = orderDto
            )
        }.onSuccess { remoteId ->
            Log.d("OrderRepositoryImpl", "Order synced with remoteId: $remoteId")
            orderDao.updateOrderSyncState(
                orderId = orderId,
                remoteId = remoteId,
                isSynced = true,
                syncStatus = SyncStatus.SYNCED,
                updatedAt = System.currentTimeMillis()
            )
        }.onFailure { error ->
            Log.d("OrderRepositoryImpl", "Error syncing order: ${error.message}")
            orderDao.updateOrderSyncState(
                orderId = orderId,
                remoteId = safeRemoteId,
                isSynced = false,
                syncStatus = SyncStatus.FAILED,
                updatedAt = System.currentTimeMillis()
            )
            throw error
        }
    }

    override suspend fun syncPendingOrders(userId: String) {
        orderDao.getUnsyncedOrdersWithItems(userId).forEach { orderWithItems ->
            runCatching {
                syncOrder(orderWithItems.order.id)
            }
        }
        pullRemoteOrders(userId)
    }

    override suspend fun pullRemoteOrders(userId: String) {
        mergeRemoteOrders(userId, remoteDS.getOrdersOnce(userId))
    }

    override fun observeRemoteOrders(userId: String): Flow<Unit> {
        return remoteDS.observeOrders(userId).map { remoteOrders ->
            // Khi có dữ liệu mới từ remote , listener bên trong sẽ emit dữ liệu mới và hàm này sẽ được gọi
            mergeRemoteOrders(userId, remoteOrders)
        }
    }

    override fun observeAllOrdersForAdmin(): Flow<List<Order>> {
        return remoteDS.observeAllOrders().map { remoteOrders ->
            remoteOrders
                .map { it.toVndNormalized().toDomainOrder() }
                .sortedByDescending { it.createdAt }
        }
    }

    override suspend fun getOrderByRemoteId(remoteId: String): Order? {
        return remoteDS.getOrderByRemoteId(remoteId)?.toDomainOrder()
    }

    override suspend fun markStockDeducted(orderId: Int) {
        orderDao.markStockDeducted(
            orderId = orderId,
            syncStatus = SyncStatus.PENDING,
            updatedAt = System.currentTimeMillis()
        )
    }

    override suspend fun updateOrderStatusForAdmin(
        remoteId: String,
        orderStatus: OrderStatus,
        paymentStatus: PaymentStatus?
    ) {
        remoteDS.updateOrderStatus(
            remoteId = remoteId,
            orderStatus = orderStatus.name,
            paymentStatus = paymentStatus?.name,
            updatedAt = System.currentTimeMillis()
        )
    }

    override suspend fun requestOrderCancellation(
        orderId: Int,
        requesterId: String,
        reason: String
    ) {
        val localOrder = getOrderById(orderId) ?: error("Không tìm thấy đơn hàng")
        check(localOrder.userId == requesterId) { "Bạn không thể hủy đơn hàng của người khác" }

        if (localOrder.remoteId.isNullOrBlank()) {
            syncOrder(orderId)
        }

        val syncedOrder = orderDao.getOrderWithItemsOrNull(orderId)?.toDomain()
            ?: error("Không tìm thấy đơn hàng")
        val remoteId = syncedOrder.remoteId ?: error("Đơn hàng chưa được đồng bộ")
        val updated = remoteDS.requestCancellation(
            remoteId = remoteId,
            requesterId = requesterId,
            reason = reason,
            updatedAt = System.currentTimeMillis()
        )
        mergeRemoteOrders(requesterId, listOf(updated))
    }

    override suspend fun cancelCreatedOrder(
        orderId: Int,
        requesterId: String,
        reason: String
    ) {
        val localOrder = getOrderById(orderId) ?: error("Không tìm thấy đơn hàng")
        check(localOrder.userId == requesterId) { "Bạn không thể hủy đơn hàng của người khác" }

        if (localOrder.remoteId.isNullOrBlank()) {
            syncOrder(orderId)
        }

        val syncedOrder = orderDao.getOrderWithItemsOrNull(orderId)?.toDomain()
            ?: error("Không tìm thấy đơn hàng")
        val remoteId = syncedOrder.remoteId ?: error("Đơn hàng chưa được đồng bộ")
        val updated = remoteDS.cancelCreatedOrder(
            remoteId = remoteId,
            requesterId = requesterId,
            reason = reason,
            updatedAt = System.currentTimeMillis()
        )
        mergeRemoteOrders(requesterId, listOf(updated))
    }

    override suspend fun approveOrderCancellation(remoteId: String, adminId: String) {
        remoteDS.approveCancellation(
            remoteId = remoteId,
            adminId = adminId,
            updatedAt = System.currentTimeMillis()
        )
    }

    override suspend fun confirmManualRefund(remoteId: String, adminId: String) {
        remoteDS.confirmManualRefund(
            remoteId = remoteId,
            adminId = adminId,
            updatedAt = System.currentTimeMillis()
        )
    }

    //  hợp nhất các đơn hàng từ remote vào local
    private suspend fun mergeRemoteOrders(userId: String, remoteOrders: List<OrderRemoteDto>) {
        remoteOrders.forEach { originalRemote ->
            val remote = originalRemote.toVndNormalized()
            val remoteId = remote.remoteId
            if (remoteId.isNullOrBlank()) return@forEach

            // dữ liệu tiền có thể thay đổi trên remote (khi dev sửa lại đơn vị tiền) ,kiểm tra để cập nhật lại
            if (remote != originalRemote) {
                remoteDS.upsertOrder(userId, remote)
            }

            val local = orderDao.getByRemoteId(remoteId)

            if (local != null && !local.isSynced) {
                val remoteEntity = remote.toEntity(existingLocalId = local.id)
                orderDao.updateRemoteStatusForDirtyOrder(
                    orderId = local.id,
                    remoteId = remoteId,
                    orderStatus = remoteEntity.orderStatus,
                    paymentStatus = remoteEntity.paymentStatus,
                    updatedAt = maxOf(local.updatedAt, remote.updatedAt)
                )
                Log.d("OrderRepositoryImpl", "Remote status merged into dirty local order: $remoteId")
                return@forEach
            }

            val orderEntity = remote.toEntity(existingLocalId = local?.id ?: 0).copy(
                userId = userId,
                remoteId = remoteId,
                isSynced = true,
                syncStatus = SyncStatus.SYNCED
            )
            orderDao.upsertRemoteOrderWithItems(
                order = orderEntity,
                orderItems = remote.items.map { it.toEntity() }
            )
            Log.d("OrderRepositoryImpl", "Remote order merged: $remoteId")
        }
    }
}
