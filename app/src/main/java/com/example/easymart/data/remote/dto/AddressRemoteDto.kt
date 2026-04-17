package com.example.easymart.data.remote.dto

data class AddressRemoteDto(
    val id: Int = 0,
    val userUid: String? = null,
    val name: String = "",
    val phone: String = "",
    val detailAddress: String = "",
    val addressString: String = "",
    val isDefault: Boolean = false,
    val provinceCode: Int = 0,
    val districtCode: Int = 0,
    val wardCode: Int = 0,
    val isDeleted: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
)

