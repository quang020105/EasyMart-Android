package com.example.easymart.data.remote.dto.payment

import com.example.easymart.data.remote.dto.OrderItemDto

data class CreateOrderRequest(
    val userId: String,
    val items: List<OrderItemDto>,
    val amount: Long
)
