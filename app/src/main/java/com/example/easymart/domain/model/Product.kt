package com.example.easymart.domain.model

data class Product(
    val id: Int,
    val name: String,
    val description: String?,
    val price: Double,
    val imageUrl: String,
    val imageRes: Int = 0,
    val images: List<ProductImage> = emptyList(),
    val category: String = "",
    val stockQuantity: Int = 0,
    val soldQuantity: Int = 0,
    val rating: ProductRating = ProductRating()
)

// Rating cho sản phẩm theo API FakeStore.
data class ProductRating(
    val rate: Double = 0.0,
    val count: Int = 0
)
