package com.example.easymart.presentation.ui.auth.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.usecase.auth.LoginUseCase
import com.example.easymart.domain.usecase.cart.MergeGuestCartIntoUserUseCase
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val mergeCartUseCase: MergeGuestCartIntoUserUseCase
) : ViewModel() {

    private val _formState = MutableStateFlow(LoginFormState())
    val formState: StateFlow<LoginFormState> = _formState

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<LoginUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onEmailChange(email: String) {
        val error = validateEmail(email)
        _formState.value = _formState.value.copy(email = email, emailError = error)
    }

    fun onPasswordChange(newPassword: String) {
        val passwordError = validatePassword(newPassword)
        _formState.value =
            _formState.value.copy(password = newPassword, passwordError = passwordError)
    }

    fun login(email: String, password: String) {
        val current = _formState.value
        val emailError = validateEmail(email)
        val passwordError = validatePassword(password)
        _formState.value = current.copy(
            emailError = emailError,
            passwordError = passwordError
        )
        //check lỗi khi người dùng chưa nhập gì mà bấm đăng nhập
        if (!current.copy(emailError = emailError, passwordError = passwordError).isValid) {
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            val result = loginUseCase(email, password)
            result
                .onSuccess { user ->
                    _events.send(LoginUiEvent.LoginSuccess(user))
                    //gộp giỏ hàng guest vào user
                    mergeCartUseCase(user.id)
                }
                .onFailure { e ->
                    val message = mapExceptionToMessage(e)
                    if (e is FirebaseAuthException) {
                        Log.e("LoginError", "code=${e.errorCode}, message=${e.message}")
                    }
                    _uiState.value = LoginUiState.Error(message)
                    _events.send(LoginUiEvent.ShowMessage(message))
                }
        }
    }

    private fun validateEmail(email: String): String? {
        val v = email.trim()
        if (v.isEmpty()) return "Email không được để trống."
        if (!Patterns.EMAIL_ADDRESS.matcher(v).matches()) return "Email không hợp lệ"
        return null
    }

    private fun validatePassword(password: String): String? {
        if (password.isEmpty()) return "Mật khẩu không được để trống"
        if (password.length < 6) return "Mật khẩu phải ít nhất 6 ký tự"
        return null
    }

    private fun mapExceptionToMessage(e: Throwable): String {
        return when (e) {
            is FirebaseAuthException -> {
                when (e.errorCode) {
                    "ERROR_INVALID_CREDENTIAL",
                    "ERROR_USER_NOT_FOUND",
                    "ERROR_WRONG_PASSWORD" -> {
                        "Email hoặc mật khẩu không đúng."
                    }

                    "ERROR_INVALID_EMAIL" -> "Email không hợp lệ."
                    else -> "Đăng nhập không thành công."
                }
            }

            is FirebaseNetworkException -> "Vui lòng kết nối mạng khi đăng nhập."
            else -> e.message ?: "Đã xảy ra lỗi"
        }
    }

    override fun onCleared() {
        super.onCleared()
        _events.close()
    }
}


