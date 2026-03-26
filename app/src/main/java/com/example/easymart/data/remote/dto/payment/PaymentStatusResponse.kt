package com.example.easymart.data.remote.dto.payment

data class PaymentStatusResponse(
    val serverOrderId: Int,
    val status: String,
    val providerRef: String?
)