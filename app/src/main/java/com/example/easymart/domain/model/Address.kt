package com.example.easymart.domain.model

data class Address(
    val tagName: String? = null,
    val userId: Int = 0,
    val name: String,
    val phone: String,
    val detailAddress: String,
    val districtCity: String
)