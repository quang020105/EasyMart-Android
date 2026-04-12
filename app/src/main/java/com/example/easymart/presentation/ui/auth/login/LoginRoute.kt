package com.example.easymart.presentation.ui.auth.login

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.example.easymart.domain.model.User

@Composable
fun LoginRoute(
    viewModel: LoginViewModel,
    onLoginClick: (email: String, sdt: String, password: String) -> Unit = {_,_,_ ->},
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit = {},
    onLoginSuccess: (user: User) -> Unit = {}
){
    val formState = viewModel.formState.collectAsState()
    val uiState = viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when(event) {
                is LoginUiEvent.LoginSuccess -> {
                    onLoginSuccess(event.user)
                }
                is LoginUiEvent.ShowMessage -> {
                    snackBarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { innerPadding ->
        val padding = innerPadding
        LoginScreen(
            formState = formState.value,
            uiState = uiState.value,
            onLoginClick = onLoginClick,
            onNavigateToSignUp = onNavigateToSignUp,
            onNavigateToForgotPassword = onNavigateToForgotPassword,
            onEmailChange = { viewModel.onEmailChange(it) },
            onPasswordChange = { viewModel.onPasswordChange(it) },
        )
    }
}