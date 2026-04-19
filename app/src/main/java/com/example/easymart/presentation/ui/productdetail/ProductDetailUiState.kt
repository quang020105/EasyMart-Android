package com.example.easymart.presentation.ui.productdetail

import com.example.easymart.domain.model.Product

data class ProductDetailUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val product: Product? = null,
    val error: String? = null,
    val refreshError: String? = null,
    val similarProducts: List<Product> = emptyList(),
    val isLoadingSimilar: Boolean = false
)
