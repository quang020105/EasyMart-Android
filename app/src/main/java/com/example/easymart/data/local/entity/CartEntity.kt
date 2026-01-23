package com.example.easymart.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carts")
data class CartEntity(
    @PrimaryKey
    val id: String,
    val userId: String?,
    val createdAt: Long,
    val updatedAt: Long
)