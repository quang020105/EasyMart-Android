package com.example.easymart.data.remote.dto.payment

data class PayOsOrderStatusResponse(
    val orderCode: Long,
    val amount: Long,
    val status: String,
    val paymentLinkId: String?,
    val checkoutUrl: String?,
    val qrCode: String?
)

