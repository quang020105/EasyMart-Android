package com.example.easymart.domain.model


//đơn hàng đơn lẻ
data class OrderItem(
    val id: Int,
    val product: Product,
    val quantity: Int,
){
    val totalPriceVnd: Long
        get() = product.priceVnd * quantity
}

