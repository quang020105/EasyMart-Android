package com.example.easymart.data.remote.dto

data class OrderRemoteDto(
    val localId: Int = 0,
    val remoteId: String? = null,
    val userId: String = "",
    val orderNumber: String = "",
    val items: List<OrderRemoteItemDto> = emptyList(),
    val totalAmount: Long = 0L,
    val subtotal: Long = totalAmount,
    val shippingFee: Long = 0L,
    val currency: String = "VND",
    val moneySchemaVersion: Int = 2,
    val orderStatus: String = "",
    val paymentStatus: String = "",
    val paymentMethod: String = "",
    val shippingName: String = "",
    val shippingPhone: String = "",
    val shippingAddressString: String = "",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val stockDeducted: Boolean = false
)

data class OrderRemoteItemDto(
    val productId: Int = 0,
    val productName: String = "",
    val productImage: String = "",
    val price: Double = 0.0,
    val priceVnd: Long = 0L,
    val quantity: Int = 0
)
