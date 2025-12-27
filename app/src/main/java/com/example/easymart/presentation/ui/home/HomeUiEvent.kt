package com.example.easymart.presentation.ui.home

import com.example.easymart.domain.model.Product

sealed class HomeUiEvent {
    data class ShowMessage(val message: String) : HomeUiEvent()
    data class NavigateToProduct(val product: Product): HomeUiEvent()
}