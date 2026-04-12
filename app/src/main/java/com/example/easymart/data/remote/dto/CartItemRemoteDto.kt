package com.example.easymart.data.remote.dto

data class CartItemRemoteDto(
    val productId: Int = 0,
    val name: String = "",
    val price: Double = 0.0,
    val imageUrl: String? = null,
    val quantity: Int = 0,
    val addAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

