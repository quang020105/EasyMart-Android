package com.example.easymart.domain.model

//tập hợp đơn hàng
data class Order(
    val id: Int,
    val userId: Int,
    val orderNumber: String,
    val items: List<OrderItem> = emptyList(),
    val totalAmount: Long,
    val status: OrderStatus = OrderStatus.PENDING,
    val shippingAddress: String = "",
    val createdAt: String = "",
    val payment: Payment? = null
)

enum class OrderStatus {
    PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
}
