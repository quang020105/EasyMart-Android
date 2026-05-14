package com.example.easymart.presentation.ui.admin.products.add_edit

data class AdminAddEditProductUiState(
    val title: String = "",
    val price: String = "",
    val description: String = "",
    val category: String = "",
    val imageUri: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val titleError: String? = null,
    val priceError: String? = null,
    val descriptionError: String? = null,
    val categoryError: String? = null,
    val imageUriError: String? = null
)
