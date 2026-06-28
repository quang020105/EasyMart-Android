package com.example.easymart.presentation.ui.category

import com.example.easymart.domain.model.Product

sealed class CategoryUiEvent {
    data class ShowMessage(val message: String) : CategoryUiEvent()
    data class NavigateToProduct(val product: Product) : CategoryUiEvent()
}
