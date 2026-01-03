package com.example.easymart.presentation.ui.resultorder

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@Composable
fun OnlinePaymentProcessingRoute(
    onPaymentFinished: () -> Unit,
    onSuccess: () -> Unit = {},
    onFailed: () -> Unit = {}
) {
    //giả định chờ xử lý thanh toán trực tuyến trong 2s
    LaunchedEffect(Unit) {
        delay(2500)
        onPaymentFinished()
    }
    OnlinePaymentProcessingScreen()
}