package com.example.easymart.presentation.ui.profile

sealed class ProfileUiEvent {
    data class Navigate(val route: String) : ProfileUiEvent()
    object LogoutSuccess : ProfileUiEvent()
    data class ShowMessage(val message: String) : ProfileUiEvent()
}