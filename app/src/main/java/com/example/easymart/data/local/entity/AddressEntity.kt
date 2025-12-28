package com.example.easymart.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "address")
data class AddressEntity(
    @PrimaryKey (autoGenerate = true)
    val id: Int = 0,
    val tagName: String? = null,
    val userId: Int = 0,
    val name: String,
    val phone: String,
    val detailAddress: String,
    val addressString: String,
    val isDefault: Boolean = false,
    //lưu id để lookup dữ liệu khi cần sửa hoặc xóa địa chỉ
    val provinceCode: Int = 0,
    val districtCode: Int = 0,
    val wardCode: Int = 0,
)
