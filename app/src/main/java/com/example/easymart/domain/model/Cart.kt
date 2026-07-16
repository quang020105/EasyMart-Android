package com.example.easymart.domain.model

data class Cart(
    val id: Int,
    val userId: Int,
    val items: List<CartItem> = emptyList(),
    val createdAt: String,
){
    val totalAmountVnd: Long
        get() = items.sumOf { it.totalPriceVnd }
}
