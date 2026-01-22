package com.example.easymart.presentation.ui.login

import com.example.easymart.domain.model.User

sealed class LoginUiEvent {
    data class ShowMessage(val message: String) : LoginUiEvent()
    data class LoginSuccess(val user: User) : LoginUiEvent()
}