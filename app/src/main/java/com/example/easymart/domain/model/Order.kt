package com.example.easymart.domain.model

//tập hợp đơn hàng
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
    val isSynced: Boolean = false,
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val stockDeducted: Boolean = false,
)

enum class OrderStatus {
    CREATED, //đã tạo đơn
    CONFIRMED, //đã xác nhận
    PACKING, //đang chuẩn bị hàng
    SHIPPING, //đang vận chuyển
    DELIVERED, //đã giao hàng
    CANCELLED // đã hủy
}

enum class SyncStatus {
    PENDING,
    SYNCING,
    SYNCED,
    FAILED
}
