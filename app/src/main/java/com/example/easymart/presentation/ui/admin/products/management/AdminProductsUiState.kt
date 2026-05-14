package com.example.easymart.presentation.ui.admin.products.management

import androidx.compose.runtime.Immutable
import com.example.easymart.domain.model.Product

@Immutable
data class AdminProductsUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val categories: List<String> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val onlyLowStock: Boolean = false,
    val lowStockThreshold: Int = 5,
    val error: String? = null
)