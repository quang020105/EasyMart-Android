package com.example.easymart.presentation.ui.search

import com.example.easymart.domain.model.Product

sealed class SearchUiEvent {
    data class ShowMessage(val message: String) : SearchUiEvent()
    data class NavigateToProduct(val product: Product) : SearchUiEvent()
}

