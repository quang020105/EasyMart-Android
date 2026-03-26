package com.example.easymart.data.remote.dto.payment

data class PayOsCreatePaymentResponse(
    val orderCode: Long,
    val status: String,
    val checkoutUrl: String,
    val qrCode: String?,
    val paymentLinkId: String?
)

