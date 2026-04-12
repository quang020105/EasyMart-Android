package com.example.easymart.presentation.ui.auth.forgot_password

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun ForgotPasswordRoute(
    viewModel: ForgotPasswordViewModel,
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    var email by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(uiState) {
        val errorMessage = (uiState as? ForgotPasswordUiState.Error)?.message
        if (!errorMessage.isNullOrBlank()) {
            snackBarHostState.showSnackbar(errorMessage)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { innerPadding ->
        val padding = innerPadding
        ForgotPasswordScreen(
            email = email,
            uiState = uiState,
            onEmailChange = {
                email = it
                viewModel.onEmailChanged(it)
            },
            onSubmitClick = { viewModel.sendPasswordResetEmail(email) },
            onNavigateToLogin = onNavigateToLogin
        )
    }
}