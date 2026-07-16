package com.example.easymart.data.remote.dto

data class ProductFirestoreDto(
    val id: Int = 0,
    val name: String = "",
    val description: String? = null,
    val price: Double = 0.0,
    val priceVnd: Long = 0L,
    val currency: String = "VND",
    val moneySchemaVersion: Int = 2,
    val imageUrl: String = "",
    val imageUrls: List<String> = emptyList(),
    val brand: String = "",
    val category: String = "",
    val ratingRate: Double = 0.0,
    val ratingCount: Int = 0,
    val stockQuantity: Int = 0,
    val soldQuantity: Int = 0,
    val isVisible: Boolean = true,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val isDeleted: Boolean = false,
    val storagePath: String? = null
)
