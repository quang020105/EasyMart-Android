package com.example.easymart.domain.model

data class Cart(
    val id: Int,
    val userId: Int,
    val items: List<CartItem> = emptyList(),
    val createdAt: String,
){
    val totalAmount: Double
        get() = items.sumOf { it.totalPrice }
}
