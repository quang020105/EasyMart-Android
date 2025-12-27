package com.example.easymart.presentation.ui.search

import com.example.easymart.domain.model.Product

data class SearchUiState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val suggestions: List<String> = emptyList(),
    val error: String? = null
)

