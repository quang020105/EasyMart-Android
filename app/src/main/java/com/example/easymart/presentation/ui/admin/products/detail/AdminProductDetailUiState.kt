package com.example.easymart.presentation.ui.admin.products.detail

import androidx.compose.runtime.Immutable
import com.example.easymart.domain.model.Product

@Immutable
data class AdminProductDetailUiState(
    val isLoading: Boolean = false,
    val product: Product? = null,
    val error: String? = null
)


