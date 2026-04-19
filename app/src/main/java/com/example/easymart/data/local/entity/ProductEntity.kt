package com.example.easymart.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String?,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val updatedAt: Long,
    val ratingRate: Double,
    val ratingCount: Int
)
