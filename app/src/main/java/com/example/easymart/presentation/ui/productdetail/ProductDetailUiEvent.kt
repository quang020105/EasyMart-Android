package com.example.easymart.presentation.ui.productdetail

sealed class ProductDetailUiEvent {
    data class ShowMessage(val message: String) : ProductDetailUiEvent()
}
