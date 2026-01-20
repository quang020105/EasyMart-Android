package com.example.easymart.presentation.ui.orderdetail

sealed class OrderDetailUiEvent {
    data class NavigateToTrack(val orderId: Int) : OrderDetailUiEvent()
    data class NavigateToReview(val orderId: Int) : OrderDetailUiEvent()
    data class NavigateToBuyAgain(val orderId: Int) : OrderDetailUiEvent()
    data class ShowToast(val message: String) : OrderDetailUiEvent()
    data class ShowConfirmCancel(val orderId: Int) : OrderDetailUiEvent()
}