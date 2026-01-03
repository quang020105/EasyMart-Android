package com.example.easymart.presentation.ui.payment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun SelectPaymentRoute (
    viewModel: SelectPaymentViewModel,
    onNavigateBack: () -> Unit
){
    val uiState = viewModel.uiState.collectAsState()

    SelectPaymentMethodScreen(
        totalAmount = 0.0,
        state = uiState.value,
        onMethodSelected = { method -> viewModel.selectPaymentMethod(method) },
        onConfirmClick = onNavigateBack
    )
}