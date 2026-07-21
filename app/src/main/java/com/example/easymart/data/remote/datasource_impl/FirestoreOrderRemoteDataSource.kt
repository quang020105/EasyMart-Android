package com.example.easymart.data.remote.datasource_impl

import com.example.easymart.data.mapper.toDomainOrder
import com.example.easymart.data.remote.datasource.OrderRemoteDataSource
import com.example.easymart.data.remote.dto.OrderRemoteDto
import com.example.easymart.data.remote.dto.OrderRemoteItemDto
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.domain.model.RefundMode
import com.example.easymart.domain.order.OrderCancellationPolicy
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.collections.get

class FirestoreOrderRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : OrderRemoteDataSource {

    private fun ordersRef() =
        firestore.collection("orders")

    private fun productsRef() = firestore.collection("products")

    override suspend fun upsertOrder(userId: String, order: OrderRemoteDto): String {
        val documentId = order.remoteId ?: throw IllegalStateException("remoteId must be generated before syncing order")
        ordersRef()
            .document(documentId)
            .set(order.copy(remoteId = documentId), SetOptions.merge())
            .await()
        return documentId
    }

    override suspend fun getOrdersOnce(userId: String): List<OrderRemoteDto> {
        val snapshot = ordersRef()
            .whereEqualTo("userId", userId)
            .get()
            .await()
        return snapshot.documents.map { it.toOrderRemoteDto() }
    }

    override fun observeOrders(userId: String): Flow<List<OrderRemoteDto>> = callbackFlow {
        val listener = ordersRef()
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.documents.orEmpty().map { it.toOrderRemoteDto() })
            }

        awaitClose { listener.remove() }
    }

    override fun observeAllOrders(): Flow<List<OrderRemoteDto>> = callbackFlow {
        val listener = ordersRef()
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.documents.orEmpty().map { it.toOrderRemoteDto() })
            }

        awaitClose { listener.remove() }
    }

    override suspend fun getOrderByRemoteId(remoteId: String): OrderRemoteDto? {
        val doc = ordersRef().document(remoteId).get().await()
        return if (doc.exists()) doc.toOrderRemoteDto() else null
    }

    override suspend fun updateOrderStatus(
        remoteId: String,
        orderStatus: String,
        paymentStatus: String?,
        updatedAt: Long
    ) {
        val updates = mutableMapOf<String, Any>(
            "orderStatus" to orderStatus,
            "updatedAt" to updatedAt
        )
        if (paymentStatus != null) {
            updates["paymentStatus"] = paymentStatus
        }
        if (orderStatus == OrderStatus.DELIVERED.name) {
            updates["deliveredAt"] = updatedAt
        }
        ordersRef().document(remoteId).update(updates).await()
    }

    override suspend fun requestCancellation(
        remoteId: String,
        requesterId: String,
        reason: String,
        updatedAt: Long
    ): OrderRemoteDto {
        require(reason.isNotBlank()) { "Vui lòng nhập lý do hủy đơn" }

        // nếu đơn hàng có dữ liệu mới thì transaction luôn nhận được,đảm bảo tính nhất quán dữ liệu
        return firestore.runTransaction { transaction ->
            val orderRef = ordersRef().document(remoteId)
            // lấy dữ liệu đơn hàng trong transaction để đảm bảo tính nhất quán (khác với orderRef.get() là luôn lấy dữ liệu mới nhất)
            val snapshot = transaction.get(orderRef)
            check(snapshot.exists()) { "Không tìm thấy đơn hàng" }

            val order = snapshot.toOrderRemoteDto()
            check(order.userId == requesterId) { "Bạn không thể hủy đơn hàng của người khác" }
            check(OrderCancellationPolicy.canCustomerRequestCancellation(order.toDomainOrder())) {
                "Đơn hàng hiện không thể gửi yêu cầu hủy"
            }

            val updated = order.copy(
                orderStatus = OrderStatus.CANCELLATION_REQUESTED.name,
                cancellationReason = reason.trim(),
                cancellationRequestedAt = updatedAt,
                cancellationRequestedBy = requesterId,
                updatedAt = updatedAt
            )
            transaction.update(
                orderRef,
                mapOf(
                    "orderStatus" to updated.orderStatus,
                    "cancellationReason" to updated.cancellationReason,
                    "cancellationRequestedAt" to updated.cancellationRequestedAt,
                    "cancellationRequestedBy" to updated.cancellationRequestedBy,
                    "updatedAt" to updated.updatedAt
                )
            )
            updated
        }.await()
    }

    override suspend fun cancelCreatedOrder(
        remoteId: String,
        requesterId: String,
        reason: String,
        updatedAt: Long
    ): OrderRemoteDto {
        return firestore.runTransaction { transaction ->
            val orderRef = ordersRef().document(remoteId)
            val orderSnapshot = transaction.get(orderRef)
            check(orderSnapshot.exists()) { "Không tìm thấy đơn hàng" }

            val order = orderSnapshot.toOrderRemoteDto()
            check(order.userId == requesterId) { "Bạn không thể hủy đơn hàng của người khác" }
            val domainOrder = order.toDomainOrder()
            check(OrderCancellationPolicy.canCustomerCancelImmediately(domainOrder)) {
                "Đơn hàng hiện không thể hủy trực tiếp"
            }

            val restoreStock = OrderCancellationPolicy.shouldRestoreStock(domainOrder)
            val productSnapshots = if (restoreStock) {
                order.items
                    .groupBy { it.productId }
                    .mapValues { (_, items) -> items.sumOf { it.quantity } }
                    .map { (productId, quantity) ->
                        val productRef = productsRef().document(productId.toString())
                        Triple(productRef, transaction.get(productRef), quantity)
                    }
            } else {
                emptyList()
            }

            productSnapshots.forEach { (productRef, productSnapshot, quantity) ->
                check(productSnapshot.exists()) { "Không tìm thấy sản phẩm ${productRef.id}" }
                val currentStock = productSnapshot.getLong("stockQuantity")?.toInt() ?: 0
                val currentSold = productSnapshot.getLong("soldQuantity")?.toInt() ?: 0
                transaction.update(
                    productRef,
                    mapOf(
                        "stockQuantity" to currentStock + quantity,
                        "soldQuantity" to (currentSold - quantity).coerceAtLeast(0),
                        "updatedAt" to updatedAt
                    )
                )
            }

            val paymentStatus = OrderCancellationPolicy.paymentStatusAfterCancellation(domainOrder.paymentStatus)
            val updated = order.copy(
                orderStatus = OrderStatus.CANCELLED.name,
                paymentStatus = paymentStatus.name,
                stockRestored = order.stockRestored || restoreStock,
                cancellationReason = reason.trim().ifBlank { null },
                cancelledAt = updatedAt,
                cancelledBy = requesterId,
                refundAmountVnd = if (paymentStatus == PaymentStatus.REFUND_REQUIRED) {
                    order.totalAmount
                } else {
                    order.refundAmountVnd
                },
                refundMode = if (paymentStatus == PaymentStatus.REFUND_REQUIRED) {
                    RefundMode.MANUAL_SIMULATION.name
                } else {
                    order.refundMode
                },
                updatedAt = updatedAt
            )
            transaction.update(
                orderRef,
                mapOf(
                    "orderStatus" to updated.orderStatus,
                    "paymentStatus" to updated.paymentStatus,
                    "stockRestored" to updated.stockRestored,
                    "cancellationReason" to updated.cancellationReason,
                    "cancelledAt" to updated.cancelledAt,
                    "cancelledBy" to updated.cancelledBy,
                    "refundAmountVnd" to updated.refundAmountVnd,
                    "refundMode" to updated.refundMode,
                    "updatedAt" to updated.updatedAt
                )
            )
            updated
        }.await()
    }

    override suspend fun approveCancellation(
        remoteId: String,
        adminId: String,
        updatedAt: Long
    ): OrderRemoteDto {
        return firestore.runTransaction { transaction ->
            val orderRef = ordersRef().document(remoteId)
            val orderSnapshot = transaction.get(orderRef)
            check(orderSnapshot.exists()) { "Không tìm thấy đơn hàng" }

            val order = orderSnapshot.toOrderRemoteDto()
            val domainOrder = order.toDomainOrder()
            check(OrderCancellationPolicy.canAdminApproveCancellation(domainOrder)) {
                "Đơn hàng hiện không thể hủy"
            }

            // đơn hàng đã được trừ tồn kho và chưa khôi phục tồn kho
            val restoreStock = OrderCancellationPolicy.shouldRestoreStock(domainOrder)

            val productSnapshots = if (restoreStock) {
                order.items
                    .groupBy { it.productId }
                    .mapValues { (_, items) -> items.sumOf { it.quantity } }
                    .map { (productId, quantity) ->
                        val productRef = productsRef().document(productId.toString())
                        Triple(productRef, transaction.get(productRef), quantity)
                    }
            } else {
                emptyList()
            }

            // cập nhật lại số lượng tồn kho và số lượng đã bán của sản phẩm
            productSnapshots.forEach { (productRef, productSnapshot, quantity) ->
                check(productSnapshot.exists()) { "Không tìm thấy sản phẩm ${productRef.id}" }
                val currentStock = productSnapshot.getLong("stockQuantity")?.toInt() ?: 0
                val currentSold = productSnapshot.getLong("soldQuantity")?.toInt() ?: 0
                transaction.update(
                    productRef,
                    mapOf(
                        "stockQuantity" to currentStock + quantity,
                        "soldQuantity" to (currentSold - quantity).coerceAtLeast(0),
                        "updatedAt" to updatedAt
                    )
                )
            }

            val paymentStatus = OrderCancellationPolicy.paymentStatusAfterCancellation(domainOrder.paymentStatus)
            val refundAmountVnd = if (paymentStatus == PaymentStatus.REFUND_REQUIRED) {
                order.totalAmount
            } else {
                order.refundAmountVnd
            }
            val updated = order.copy(
                orderStatus = OrderStatus.CANCELLED.name,
                paymentStatus = paymentStatus.name,
                stockRestored = order.stockRestored || restoreStock,
                cancelledAt = updatedAt,
                cancelledBy = adminId,
                refundAmountVnd = refundAmountVnd,
                refundMode = if (paymentStatus == PaymentStatus.REFUND_REQUIRED) {
                    RefundMode.MANUAL_SIMULATION.name
                } else {
                    order.refundMode
                },
                updatedAt = updatedAt
            )
            transaction.update(
                orderRef,
                mapOf(
                    "orderStatus" to updated.orderStatus,
                    "paymentStatus" to updated.paymentStatus,
                    "stockRestored" to updated.stockRestored,
                    "cancelledAt" to updated.cancelledAt,
                    "cancelledBy" to updated.cancelledBy,
                    "refundAmountVnd" to updated.refundAmountVnd,
                    "refundMode" to updated.refundMode,
                    "updatedAt" to updated.updatedAt
                )
            )
            updated
        }.await()
    }

    override suspend fun confirmManualRefund(
        remoteId: String,
        adminId: String,
        updatedAt: Long
    ): OrderRemoteDto {
        return firestore.runTransaction { transaction ->
            val orderRef = ordersRef().document(remoteId)
            val snapshot = transaction.get(orderRef)
            check(snapshot.exists()) { "Không tìm thấy đơn hàng" }

            val order = snapshot.toOrderRemoteDto()
            check(order.orderStatus == OrderStatus.CANCELLED.name) { "Chỉ có thể hoàn tiền cho đơn đã hủy" }
            check(order.paymentStatus == PaymentStatus.REFUND_REQUIRED.name) { "Đơn hàng này không cần hoàn tiền" }

            val updated = order.copy(
                paymentStatus = PaymentStatus.REFUNDED.name,
                refundMode = RefundMode.MANUAL_SIMULATION.name,
                refundedAt = updatedAt,
                refundedBy = adminId,
                updatedAt = updatedAt
            )
            transaction.update(
                orderRef,
                mapOf(
                    "paymentStatus" to updated.paymentStatus,
                    "refundMode" to updated.refundMode,
                    "refundedAt" to updated.refundedAt,
                    "refundedBy" to updated.refundedBy,
                    "updatedAt" to updated.updatedAt
                )
            )
            updated
        }.await()
    }

    private fun DocumentSnapshot.toOrderRemoteDto(): OrderRemoteDto {
        val dto = toObject(OrderRemoteDto::class.java)
        if (dto != null) {
            return dto.copy(
                remoteId = dto.remoteId ?: id,
                subtotal = dto.subtotal.takeIf { it > 0L } ?: dto.totalAmount
            )
        }

        return OrderRemoteDto(
            localId = getLong("localId")?.toInt() ?: 0,
            remoteId = getString("remoteId") ?: id,
            userId = getString("userId").orEmpty(),
            orderNumber = getString("orderNumber").orEmpty(),
            items = (get("items") as? List<*>)
                ?.mapNotNull { item -> item as? Map<*, *> }
                ?.map { item ->
                    OrderRemoteItemDto(
                        productId = (item["productId"] as? Number)?.toInt() ?: 0,
                        productName = item["productName"] as? String ?: "",
                        productImage = item["productImage"] as? String ?: "",
                        price = (item["price"] as? Number)?.toDouble() ?: 0.0,
                        priceVnd = (item["priceVnd"] as? Number)?.toLong() ?: 0L,
                        quantity = (item["quantity"] as? Number)?.toInt() ?: 0
                    )
                }
                ?: emptyList(),
            totalAmount = getLong("totalAmount") ?: 0L,
            subtotal = getLong("subtotal") ?: getLong("totalAmount") ?: 0L,
            shippingFee = getLong("shippingFee") ?: 0L,
            currency = getString("currency") ?: "VND",
            moneySchemaVersion = (get("moneySchemaVersion") as? Number)?.toInt() ?: 1,
            orderStatus = getString("orderStatus").orEmpty(),
            paymentStatus = getString("paymentStatus").orEmpty(),
            paymentMethod = getString("paymentMethod").orEmpty(),
            shippingName = getString("shippingName").orEmpty(),
            shippingPhone = getString("shippingPhone").orEmpty(),
            shippingAddressString = getString("shippingAddressString").orEmpty(),
            createdAt = getLong("createdAt") ?: 0L,
            updatedAt = getLong("updatedAt") ?: 0L,
            deliveredAt = getLong("deliveredAt"),
            stockDeducted = getBoolean("stockDeducted") ?: false,
            stockRestored = getBoolean("stockRestored") ?: false,
            cancellationReason = getString("cancellationReason"),
            cancellationRequestedAt = getLong("cancellationRequestedAt"),
            cancellationRequestedBy = getString("cancellationRequestedBy"),
            cancelledAt = getLong("cancelledAt"),
            cancelledBy = getString("cancelledBy"),
            refundAmountVnd = getLong("refundAmountVnd") ?: 0L,
            refundMode = getString("refundMode"),
            refundedAt = getLong("refundedAt"),
            refundedBy = getString("refundedBy")
        )
    }
}
