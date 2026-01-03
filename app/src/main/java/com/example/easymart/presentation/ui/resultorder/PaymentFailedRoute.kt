package com.example.easymart.presentation.ui.resultorder

import androidx.compose.runtime.Composable

@Composable
fun PaymentFailedRoute(
    message: String,
    onRetry: () -> Unit,
    onChangePaymentMethod: () -> Unit,
    onBackToCheckOut: () -> Unit
){
    PaymentFailedScreen(
        message = message,
        onRetry = onRetry,
        onChangePaymentMethod = onChangePaymentMethod,
        onBackToCheckOut = onBackToCheckOut
    )
}