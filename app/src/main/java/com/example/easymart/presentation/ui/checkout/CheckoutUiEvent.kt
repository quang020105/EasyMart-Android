package com.example.easymart.presentation.ui.checkout

import com.example.easymart.domain.model.PaymentMethod

sealed class CheckoutUiEvent {
    object NavigateToSuccess : CheckoutUiEvent()
    data class NavigateToOnlineProcessing(
        val checkoutUrl: String?,
        val orderCode: Long?,
        val localOrderId: Int
    ) : CheckoutUiEvent()
    object NavigateToSelectAddress: CheckoutUiEvent()
    object NavigateSelectPaymentMethod: CheckoutUiEvent()
    data class NavigateToPaymentFailed(
        val orderId: Int,
        val reason: String
    ): CheckoutUiEvent()
    data class ShowErrorMessage(val message: String) : CheckoutUiEvent()
}