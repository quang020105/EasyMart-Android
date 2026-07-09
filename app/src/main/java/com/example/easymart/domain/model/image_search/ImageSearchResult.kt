package com.example.easymart.domain.model.image_search

data class ImageSearchResult(
    val products: List<SimilarProduct> = emptyList(),
    val confidence: Double = 0.0
)
