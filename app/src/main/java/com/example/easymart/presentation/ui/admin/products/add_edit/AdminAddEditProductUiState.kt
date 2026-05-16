package com.example.easymart.presentation.ui.admin.products.add_edit

data class AdminAddEditProductUiState(
    val productId: Int? = null,
    val isEdit: Boolean = false,
    val createdAt: Long = 0L,
    val ratingRate: Double = 0.0,
    val ratingCount: Int = 0,
    val isVisible: Boolean = true,
    val storagePath: String? = null,
    val title: String = "",
    val price: String = "",
    val description: String = "",
    val category: String = "",
    val quantity: String = "",
    val imageUri: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val titleError: String? = null,
    val priceError: String? = null,
    val descriptionError: String? = null,
    val categoryError: String? = null,
    val quantityError: String? = null,
    val imageUriError: String? = null
)
