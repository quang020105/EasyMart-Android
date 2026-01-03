package com.example.easymart.presentation.ui.checkout

import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.PaymentMethod

data class CheckoutUiState(
    val isProcessing: Boolean = false,
    val errorMessage: String? = null,
    val order: Order? = null,
)
