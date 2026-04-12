package com.example.easymart.presentation.ui.auth.forgot_password

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.usecase.auth.SendPasswordResetEmailUseCase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val sendPasswordResetEmailUseCase: SendPasswordResetEmailUseCase
) : ViewModel() {
    private val _uiState =
        MutableStateFlow<ForgotPasswordUiState>(ForgotPasswordUiState.Idle)
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState

    fun onEmailChanged(email: String) {
        val isValid = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if (_uiState.value is ForgotPasswordUiState.Error && isValid) {
            _uiState.value = ForgotPasswordUiState.Idle
        } else if (!isValid) {
            _uiState.value = ForgotPasswordUiState.Error("Email không hợp lệ")
        }
    }

    fun sendPasswordResetEmail(email: String) {
        if (email.isEmpty()) {
            _uiState.value = ForgotPasswordUiState.Error("Vui lòng nhập email")
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = ForgotPasswordUiState.Error("Email không hợp lệ")
            return
        }

        _uiState.value = ForgotPasswordUiState.Loading

        viewModelScope.launch {
            val result = sendPasswordResetEmailUseCase(email)

            result.onSuccess {
                Log.d("RESET", "SUCCESS")
                _uiState.value = ForgotPasswordUiState.Success
            }.onFailure { e ->
                Log.e("RESET", "ERROR: ${e.message}")
                _uiState.value = ForgotPasswordUiState.Error(mapError(e))
            }
        }
    }

    private fun mapError(e: Throwable): String {
        return when (e) {
            is FirebaseAuthInvalidUserException -> "Email không tồn tại"
            is FirebaseAuthInvalidCredentialsException -> "Email không hợp lệ"
            else -> "Không thể gửi email. Vui lòng thử lại"
        }
    }
}