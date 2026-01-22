package com.example.easymart.presentation.ui.signup

import com.example.easymart.domain.model.User

sealed class SignUpUiEvent {
    data class ShowMessage(val message: String) : SignUpUiEvent()
    data class SignUpSuccess(val user: User) : SignUpUiEvent()
}