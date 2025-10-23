package com.example.easymart.domain.model

data class CartItem(
    val id: Int,
    val product: Product,
    val quantity: Int,
    val price: Double
){
    val totalPrice: Double
        get() = price * quantity
}

