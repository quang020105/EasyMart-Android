package com.example.easymart.presentation.ui.order

import com.example.easymart.domain.model.Order

sealed class OrderListUiState {
    object Loading : OrderListUiState()
    data class Success(val orders: List<Order>) : OrderListUiState()
    object Empty : OrderListUiState()
    data class Error(val message: String) : OrderListUiState()
}