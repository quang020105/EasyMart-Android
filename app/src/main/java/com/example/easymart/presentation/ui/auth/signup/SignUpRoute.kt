package com.example.easymart.presentation.ui.auth.signup

import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.easymart.domain.model.User

@Composable
fun SignUpRoute(
    viewModel: SignUpViewModel,
    onSignUpClick: () -> Unit = {},
    onNavigateToLogin: () -> Unit,
    onSignUpSuccess: (user: User) -> Unit = {}
) {
    val formState by viewModel.formState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SignUpUiEvent.SignUpSuccess -> {
                    onSignUpSuccess(event.user)
                    Log.d("SignUpRoute", "Người dùng đăng kí : ${event.user}")
                }

                is SignUpUiEvent.ShowMessage -> {
                    // Handle sign-up error if needed
                    snackBarHostState.showSnackbar(event.message)
                    Log.d("SignUpRoute", "Lỗi đăng kí: ${event.message}")
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { innerPadding ->
        val padding = innerPadding
        SignUpScreen(
            formState = formState,
            uiState = uiState,
            onSignUpClick = onSignUpClick,
            onNavigateToLogin = onNavigateToLogin,
            onFullNameChange = { viewModel.onFullNameChange(it) },
            onEmailChange = { viewModel.onEmailChange(it) },
            onPhoneChange = { viewModel.onPhoneChange(it) },
            onPasswordChange = { viewModel.onPasswordChange(it) },
            onConfirmPasswordChange = { viewModel.onConfirmPasswordChange(it) }
        )
    }
}