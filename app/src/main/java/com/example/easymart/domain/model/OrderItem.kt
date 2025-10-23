package com.example.easymart.domain.model


//đơn hàng đơn lẻ
data class OrderItem(
    val id: Int,
    val product: Product,
    val quantity: Int,
){
    val totalPrice: Double
        get() = product.price * quantity
}

