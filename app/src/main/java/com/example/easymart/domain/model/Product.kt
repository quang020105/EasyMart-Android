package com.example.easymart.domain.model

data class Product(
    val id: Int,
    val name: String,
    val description: String?,
    val price: Double,
    val imageUrl: String,
    val imageRes: Int,
    val images: List<ProductImage> = emptyList(),
    val categories: List<Category>? = emptyList(),
    val stockQuantity: Int = 0,
    val soldQuantity: Int = 0
)
