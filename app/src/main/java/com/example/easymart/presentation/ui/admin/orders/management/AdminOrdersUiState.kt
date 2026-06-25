package com.example.easymart.presentation.ui.admin.orders.management

import androidx.compose.runtime.Immutable
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentStatus

@Immutable
data class AdminOrdersUiState(
    val isLoading: Boolean = false,
    val orders: List<AdminOrdersUiModel> = emptyList(),
    val searchQuery: String = "",
    val selectedOrderStatus: OrderStatus? = null,
    val selectedPaymentMethod: PaymentMethod? = null,
    val selectedPaymentStatus: PaymentStatus? = null,
    val errorMessage: String? = null
)