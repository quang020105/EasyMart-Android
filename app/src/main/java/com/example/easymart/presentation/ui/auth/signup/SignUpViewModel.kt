package com.example.easymart.presentation.ui.auth.signup

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.usecase.auth.SignUpUseCase
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.isEmpty

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {
    private val _formState = MutableStateFlow(SignUpFormState())
    val formState = _formState.asStateFlow()

    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<SignUpUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onFullNameChange(value: String) {
        _formState.value = _formState.value.copy(
            fullName = value,
            fullNameError = validateFullName(value)
        )
    }

    fun onEmailChange(value: String) {
        _formState.value = _formState.value.copy(
            email = value,
            emailError = validateEmail(value)
        )
    }

    fun onPhoneChange(value: String) {
        _formState.value = _formState.value.copy(
            phone = value,
            phoneError = validatePhone(value)
        )
    }

    fun onPasswordChange(value: String) {
        _formState.value = _formState.value.copy(
            password = value,
            passwordError = validatePassword(value)
        )
    }

    fun onConfirmPasswordChange(value: String) {
        val password = _formState.value.password
        _formState.value = _formState.value.copy(
            confirmPassword = value,
            confirmPasswordError = validateConfirmPassword(password, value)
        )
    }

    fun signUp() {
        val current = _formState.value
        val newState = current.copy(
            fullNameError = validateFullName(current.fullName),
            emailError = validateEmail(current.email),
            phoneError = validatePhone(current.phone),
            passwordError = validatePassword(current.password),
            confirmPasswordError = validateConfirmPassword(
                current.password,
                current.confirmPassword
            )
        )
        _formState.value = newState
        Log.d("SignUpViewModel", "Đã cập nhật trạng thái: ${_formState.value}")
        //check lỗi khi người dùng chưa nhập gì mà bấm đăng kí
        if (!newState.isValid) {
            return
        }

        viewModelScope.launch {
            _uiState.value = SignUpUiState.Loading
            val result = signUpUseCase(current.email, current.password, current.fullName)
            result
                .onSuccess { user ->
                    _events.send(SignUpUiEvent.SignUpSuccess(user))
                }
                .onFailure { e ->
                    val message = mapExceptionToMessage(e)
                    if (e is FirebaseAuthException) {
                        Log.e("SignUpError", "code=${e.errorCode}, message=${e.message}")
                    }
                    _uiState.value = SignUpUiState.Error(message)
                    _events.send(SignUpUiEvent.ShowMessage(message))
                }
        }
    }

    private fun mapExceptionToMessage(e: Throwable): String {
        return when (e) {
            is FirebaseAuthException -> {
                when (e.errorCode) {
                    "ERROR_INVALID_EMAIL" -> "Địa chỉ email không đúng định dạng."
                    "ERROR_EMAIL_ALREADY_IN_USE" -> "Địa chỉ email này hiện đang được sử dụng bởi một tài khoản khác."
                    "ERROR_WEAK_PASSWORD" -> "Mật khẩu quá yếu. Vui lòng chọn mật khẩu mạnh hơn."
                    else -> "Đăng kí không thành công."
                }
            }

            is FirebaseNetworkException -> "Vui lòng kết nối mạng khi đăng kí."
            else -> e.message ?: "Đã xảy ra lỗi."
        }
    }

    private fun validateFullName(fullName: String): String? {
        if (fullName.isEmpty()) return "Tên không được để trống"
        return null
    }

    private fun validatePhone(phone: String): String? {
        if (phone.isEmpty()) return "Số điện thoại không được để trống"

        val phoneRegex = Regex("^(0[1-9][0-9]{8}|\\+84[1-9][0-9]{8})$")
        if (!phoneRegex.matches(phone)) return "Số điện thoại không hợp lệ"
        return null
    }

    private fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        if (confirmPassword != password) return "Mật khẩu xác nhận không khớp"
        return null
    }

    private fun validateEmail(email: String): String? {
        if (email.isEmpty()) return "Email không được để trống"
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return "Email không hợp lệ"
        return null
    }

    private fun validatePassword(password: String): String? {
        if (password.length < 6) return "Mật khẩu tối thiểu 6 ký tự"
        return null
    }

    override fun onCleared() {
        super.onCleared()
        _events.close()
    }
}