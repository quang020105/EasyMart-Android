package com.example.easymart.data.remote.dto.payment

data class InitPaymentResponse(
    val serverOrderId: Int,
    val provider: String,
    val deeplink: String?,
    val qrImageUrl: String?
)