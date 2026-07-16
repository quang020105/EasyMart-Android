package com.example.easymart.domain.model

data class Product(
    val id: Int,
    val name: String,
    val description: String?,
    val priceVnd: Long,
    val imageUrl: String,
    val imageRes: Int = 0,
    val images: List<ProductImage> = emptyList(),
    val imageUrls: List<String> = emptyList(),
    val brand: String = "",
    val category: String = "",
    val stockQuantity: Int = 0,
    val soldQuantity: Int = 0,
    val rating: ProductRating = ProductRating(),
    val isVisible: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val isSynced: Boolean = true,
    val storagePath: String? = null,
    val localImageUri: String? = null,
    val localImageUris: List<String> = emptyList()
)

// Rating cho sản phẩm theo API FakeStore.
data class ProductRating(
    val rate: Double = 0.0,
    val count: Int = 0
)
