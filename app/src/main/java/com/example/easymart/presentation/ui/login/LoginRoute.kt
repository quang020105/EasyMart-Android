package com.example.easymart.presentation.ui.login

import androidx.compose.runtime.Composable

@Composable
fun LoginRoute(
    onLoginClick: (email: String, sdt: String, password: String) -> Unit = {_,_,_ ->},
    onNavigateToSignUp: () -> Unit
){
    LoginScreen(
        onLoginClick = onLoginClick,
        onNavigateToSignUp = onNavigateToSignUp
    )
}