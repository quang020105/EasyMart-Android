package com.example.easymart.domain.model

data class CartItem(
    val id: Int,
    val product: Product,
    val quantity: Int,
    val unitPriceVnd: Long,
    val addAt: Long = System.currentTimeMillis(),
    val isChecked: Boolean = false
){
    val totalPriceVnd: Long
        get() = unitPriceVnd * quantity
}

