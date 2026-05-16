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
    val error: String? = null,
    val sourceFilter: ProductSourceFilter = ProductSourceFilter.ALL,
    val sortType: AdminProductSort = AdminProductSort.UPDATED_DESC
)

enum class ProductSourceFilter {
    ALL,
    API,
    ADDED
}

enum class AdminProductSort {
    UPDATED_DESC,
    NAME_ASC,
    PRICE_ASC
}
