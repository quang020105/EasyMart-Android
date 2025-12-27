package com.example.easymart.domain.model

data class Address(
    val id: Int = 0,
    val userId: Int = 0,
    val name: String,
    val phone: String,
    val detailAddress: String,
    val districtCity: String,
    val isDefault: Boolean = false,
    val provinceCode: Int = 0,
    val districtCode: Int = 0,
    val wardCode: Int = 0,
){
    val tagName: String?
        get() = if(isDefault) "Mặc định" else null
}