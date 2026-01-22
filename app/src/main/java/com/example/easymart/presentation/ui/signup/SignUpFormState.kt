package com.example.easymart.presentation.ui.signup

data class SignUpFormState(
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val confirmPassword: String = "",

    val fullNameError: String? = null,
    val emailError: String? = null,
    val phoneError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
) {
    val isValid: Boolean
        get() = listOf(
            fullNameError,
            emailError,
            phoneError,
            passwordError,
            confirmPasswordError
        ).all { it == null }
}
