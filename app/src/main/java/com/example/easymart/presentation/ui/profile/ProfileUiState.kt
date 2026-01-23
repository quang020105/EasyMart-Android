package com.example.easymart.presentation.ui.profile

import com.example.easymart.domain.model.User

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
)
