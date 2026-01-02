package com.example.easymart.presentation.ui.payment

import com.example.easymart.domain.model.Payment
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentResult

data class PaymentUiState(
    val selectedMethod: PaymentMethod? = null,
    val isProcessing: Boolean = false,
    val lastResult: PaymentResult? = null,
    val walletBalance: Long = 0L
)
