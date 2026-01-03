package com.example.easymart.presentation.ui.checkout

import com.example.easymart.domain.model.PaymentMethod

sealed class CheckoutUiEvent {
    object NavigateToSuccess : CheckoutUiEvent()
    object NavigateToOnlineProcessing: CheckoutUiEvent()
    object NavigateToSelectAddress: CheckoutUiEvent()
    object NavigateSelectPaymentMethod: CheckoutUiEvent()
    data class ShowErrorMessage(val message: String) : CheckoutUiEvent()
}