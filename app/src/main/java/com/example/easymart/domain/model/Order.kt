package com.example.easymart.domain.model

//tập hợp đơn hàng
data class Order(
    val id: Int = 0,
    val userId: String,
    val orderNumber: String,
    val items: List<OrderItem> = emptyList(),
    val totalAmount: Long,
    val status: OrderStatus = OrderStatus.CREATED,
    val paymentStatus: PaymentStatus = PaymentStatus.UNPAID,
    val paymentMethod: PaymentMethod = PaymentMethod.COD,
    val shippingAddress: Address,
    val createdAt: Long = System.currentTimeMillis(),
)

enum class OrderStatus {
    CREATED, //đã tạo đơn
    CONFIRMED, //đã xác nhận
    PROCESSING, //đang chuẩn bị hàng
    SHIPPING, //đang vận chuyển
    DELIVERED, //đã giao hàng
    CANCELLED // đã hủy
}
