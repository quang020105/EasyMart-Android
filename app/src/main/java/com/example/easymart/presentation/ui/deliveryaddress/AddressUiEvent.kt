package com.example.easymart.presentation.ui.deliveryaddress

sealed class AddressUiEvent {
    data class ShowMessage(val message: String) : AddressUiEvent()
}