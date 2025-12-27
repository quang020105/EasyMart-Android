package com.example.easymart.presentation.ui.home

import com.example.easymart.domain.model.Product

data class HomeUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null
)
