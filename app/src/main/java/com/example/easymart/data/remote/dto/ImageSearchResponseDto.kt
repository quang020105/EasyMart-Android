package com.example.easymart.data.remote.dto

data class ImageSearchResponseDto(
    val success: Boolean = false,
    val message: String = "",
    val data: ImageSearchDataDto? = null
)

data class ImageSearchDataDto(
    val confidence: Double = 0.0,
    val products: List<ImageSearchProductDto> = emptyList()
)

data class ImageSearchProductDto(
    val id: Int = 0,
    val name: String = "",
    val title: String = "",
    val price: Double = 0.0,
    val priceVnd: Long = 0L,
    val currency: String = "VND",
    val description: String? = null,
    val brand: String = "",
    val category: String = "",
    val imageUrl: String = "",
    val image: String = "",
    val imageUrls: List<String> = emptyList(),
    val rating: ImageSearchRatingDto = ImageSearchRatingDto(),
    val score: Double = 0.0
)

data class ImageSearchRatingDto(
    val rate: Double = 0.0,
    val count: Int = 0
)
