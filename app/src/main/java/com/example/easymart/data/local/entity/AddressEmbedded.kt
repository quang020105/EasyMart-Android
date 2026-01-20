package com.example.easymart.data.local.entity

//lớp dùng để nhúng thông tin địa chỉ vào các thực thể khác (Order, ... )
data class AddressEmbedded(
    val name: String,
    val phone: String,
    val addressString: String
)