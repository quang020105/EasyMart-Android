package com.example.easymart.presentation.ui.admin.orders.detail

import androidx.compose.runtime.Immutable

@Immutable
data class AdminOrderDetailUiState(
    val isLoading: Boolean = false,
    val isActionLoading: Boolean = false,
    val order: AdminOrderDetailUiModel? = null,
    val errorMessage: String? = null
)
