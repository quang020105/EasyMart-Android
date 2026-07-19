package com.example.easymart.presentation.ui.orderdetail

import com.example.easymart.domain.model.Order

sealed class OrderDetailUiState {
    object Loading : OrderDetailUiState()
    data class Success(val order: Order) : OrderDetailUiState()
    data class Error(val message: String) : OrderDetailUiState()
}

data class OrderCancellationUiState(
    val pendingOrderId: Int? = null,
    val reason: String = "",
    val isSubmitting: Boolean = false,
    val mode: CustomerCancellationMode = CustomerCancellationMode.REQUEST
)

enum class CustomerCancellationMode {
    DIRECT,
    REQUEST
}
