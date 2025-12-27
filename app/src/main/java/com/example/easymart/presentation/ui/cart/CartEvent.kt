package com.example.easymart.presentation.ui.cart

sealed class CartEvent {
    data class ShowMessage(val message: String): CartEvent()
}