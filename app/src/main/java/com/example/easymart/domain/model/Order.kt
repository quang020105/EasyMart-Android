package com.example.easymart.domain.model

data class Order(
    val id: Int = 0,
    val remoteId: String? = null,
    val userId: String,
    val orderNumber: String,
    val items: List<OrderItem> = emptyList(),
    val totalAmount: Long,
    val subtotal: Long = totalAmount,
    val shippingFee: Long = 0L,
    val status: OrderStatus = OrderStatus.CREATED,
    val paymentStatus: PaymentStatus = PaymentStatus.UNPAID,
    val paymentMethod: PaymentMethod = PaymentMethod.COD,
    val shippingAddress: Address,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = createdAt,
    val deliveredAt: Long? = null,
    val isSynced: Boolean = false,
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val stockDeducted: Boolean = false, // tồn kho đã được trừ hay chưa, tránh trừ nhiều lần
    val stockRestored: Boolean = false, // tồn kho đã được khôi phục hay chưa
    val cancellationReason: String? = null, // lý do hủy đơn
    val cancellationRequestedAt: Long? = null,
    val cancellationRequestedBy: String? = null,
    val cancelledAt: Long? = null,
    val cancelledBy: String? = null,
    val refundAmountVnd: Long = 0L,
    val refundMode: RefundMode? = null,
    val refundedAt: Long? = null,
    val refundedBy: String? = null,
)

enum class OrderStatus {
    CREATED,
    CONFIRMED,
    PACKING,
    SHIPPING,
    DELIVERED,
    CANCELLATION_REQUESTED,
    CANCELLED,
}

enum class SyncStatus {
    PENDING,
    SYNCING,
    SYNCED,
    FAILED,
}

enum class RefundMode {
    MANUAL_SIMULATION,
}
