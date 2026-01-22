package com.example.easymart.presentation.auth

import com.example.easymart.domain.model.User

//quản lý đã đăng nhập hay khách
sealed class AuthState {
    object Guest : AuthState()
    data class LoggedIn(val user: User) : AuthState()
}