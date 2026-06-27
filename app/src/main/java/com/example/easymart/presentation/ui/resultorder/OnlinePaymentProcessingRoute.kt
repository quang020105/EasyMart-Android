package com.example.easymart.presentation.ui.resultorder

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.presentation.ui.checkout.CheckoutViewModel

@Composable
fun OnlinePaymentProcessingRoute(
    orderCode: Long?,
    localOrderId: Int?,
    onSuccess: () -> Unit,
    onFailed: (reason: String) -> Unit,
    onPendingTimeout: () -> Unit = {}
) {
    val viewModel = hiltViewModel<CheckoutViewModel>()

    Log.d("OnlinePaymentProcessing", "OrderCode: $orderCode, LocalOrderId: $localOrderId")
    LaunchedEffect(orderCode, localOrderId) {
        if (orderCode == null || localOrderId == null) {
            // Không đủ dữ liệu để polling
            onPendingTimeout()
            return@LaunchedEffect
        }

        val status = viewModel.pollPayOsAndUpdate(orderCode, localOrderId)

        Log.d("OnlinePaymentProcessing", "Payment status: $status")
        when (status) {
            PaymentStatus.PAID -> onSuccess()
            PaymentStatus.FAILED -> onFailed("Thanh toán thất bại hoặc đã bị huỷ")
            else -> onPendingTimeout()
        }
    }

    OnlinePaymentProcessingScreen()
}