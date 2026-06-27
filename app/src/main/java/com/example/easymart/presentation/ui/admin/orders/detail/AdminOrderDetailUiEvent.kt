package com.example.easymart.presentation.ui.admin.orders.detail

sealed class AdminOrderDetailUiEvent {
    data class ShowMessage(val message: String) : AdminOrderDetailUiEvent()
}
