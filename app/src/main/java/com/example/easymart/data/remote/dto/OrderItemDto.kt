package com.example.easymart.data.remote.dto

data class OrderItemDto(
    val productId: Int,
    val quantity: Int,
    val priceVnd: Long
)
