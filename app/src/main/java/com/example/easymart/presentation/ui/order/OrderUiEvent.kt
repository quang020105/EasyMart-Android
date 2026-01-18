package com.example.easymart.presentation.ui.order

sealed class OrderUiEvent {
    data class NavigateToOrderDetail(val orderId: Int) : OrderUiEvent()
    data class NavigateToTrack(val orderId: Int) : OrderUiEvent()
    data class NavigateToBuyAgain(val orderId: Int) : OrderUiEvent()
    data class ShowMessage(val message: String) : OrderUiEvent()
}

enum class OrderAction {
    BUY_AGAIN, // mua lại
    CANCEL, // hủy đơn hàng
    TRACK, // theo dõi đơn hàng
    VIEW_DETAIL, // xem chi tiết đơn hàng
}