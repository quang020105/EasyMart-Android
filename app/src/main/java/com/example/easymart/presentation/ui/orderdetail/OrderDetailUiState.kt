package com.example.easymart.presentation.ui.orderdetail

import com.example.easymart.domain.model.Order

sealed class OrderDetailUiState {
    object Loading : OrderDetailUiState()
    data class Success(val order: Order) : OrderDetailUiState()
    data class Error(val message: String) : OrderDetailUiState()
}