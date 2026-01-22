package com.example.easymart.presentation.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.model.User
import com.example.easymart.domain.repository.AuthRepository
import com.example.easymart.domain.usecase.auth.GetCurrentUserUseCase
import com.example.easymart.domain.usecase.auth.LoginUseCase
import com.example.easymart.domain.usecase.auth.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


// viewmodel dùng để quản lý trạng thái đăng nhập toàn cục
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Guest)
    val authState = _authState.asStateFlow()
    init {
        refresh()
    }

    fun refresh() {
        val current = getCurrentUserUseCase()
        _authState.value = if (current != null) AuthState.LoggedIn(current) else AuthState.Guest
    }

    fun onUserLoggedIn(user: User) {
        _authState.value = AuthState.LoggedIn(user)
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _authState.value = AuthState.Guest
        }
    }

}