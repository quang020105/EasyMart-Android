package com.example.easymart.presentation.ui.signup

import androidx.compose.runtime.Composable

@Composable
fun SignUpRoute(
    onSignUpClick: (fullName: String, email: String, sdt: String, password: String) -> Unit = {_,_,_,_ ->},
    onNavigateToLogin: () -> Unit
){
    SignUpScreen(
        onSignUpClick = onSignUpClick,
        onNavigateToLogin = onNavigateToLogin
    )
}