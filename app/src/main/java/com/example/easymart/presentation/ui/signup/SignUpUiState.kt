package com.example.easymart.presentation.ui.signup

import com.example.easymart.domain.model.User

sealed class SignUpUiState {
    object Idle : SignUpUiState()
    object Loading : SignUpUiState()
    data class Error(val message: String) : SignUpUiState()
}