package com.example.easymart.presentation.ui.productdetail

import com.example.easymart.domain.model.Product

data class ProductDetailUiState(
    val isLoading: Boolean = false,
    val isLoadingSimilar: Boolean = false,
    val product: Product? = null,
    val similarProducts: List<Product> = emptyList(),
    val error: String? = null
)
