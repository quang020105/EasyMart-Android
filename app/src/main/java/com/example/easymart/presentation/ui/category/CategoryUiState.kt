package com.example.easymart.presentation.ui.category

import androidx.compose.runtime.Immutable
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.ui.category.components.CustomerProductSort
import com.example.easymart.presentation.ui.category.components.CustomerProductStockFilter

@Immutable
data class CategoryUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val allProducts: List<Product> = emptyList(),
    val products: List<Product> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: String? = null,
    val searchQuery: String = "",
    val sort: CustomerProductSort = CustomerProductSort.FEATURED,
    val stockFilter: CustomerProductStockFilter = CustomerProductStockFilter.ALL,
    val error: String? = null,
    val refreshError: String? = null
)
