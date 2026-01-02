package com.example.easymart.presentation.ui.payment

import com.example.easymart.domain.model.PaymentMethod

sealed class PaymentUiEvent {
    data class SelectPaymentMethod(val method: PaymentMethod): PaymentUiEvent()
    data class Pay(val orderId: Int): PaymentUiEvent()
}