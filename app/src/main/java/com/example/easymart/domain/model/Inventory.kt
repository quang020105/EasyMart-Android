package com.example.easymart.domain.model

data class Inventory(
    val id: Int,
    val productId: Int,
    val stockQuantity: Int,
)