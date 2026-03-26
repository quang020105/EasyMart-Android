package com.example.easymart.data.remote.dto.payment

data class CreateOrderResponse(
    val serverOrderId: Int,
    val status: String
)