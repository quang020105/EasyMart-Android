package com.example.easymart.presentation.ui.resultorder

import androidx.compose.runtime.Composable

@Composable
fun OnlinePaymentProcessingRoute(
    onPaymentFinished: () -> Unit,
    onSuccess: () -> Unit = {},
    onFailed: () -> Unit = {}
) {
    OnlinePaymentProcessingScreen()
}