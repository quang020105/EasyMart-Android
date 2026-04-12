package com.example.easymart.presentation.ui.auth.login

data class LoginFormState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
){
    val isValid: Boolean
        get() = emailError.isNullOrBlank() && passwordError.isNullOrBlank()
                && email.isNotBlank() && password.isNotBlank()
}
