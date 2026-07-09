package com.example.easymart.domain.model.image_search

import com.example.easymart.domain.model.Product

data class SimilarProduct(
    val product: Product,
    val score: Double = 0.0
)
