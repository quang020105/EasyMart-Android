package com.example.easymart.presentation.ui.payment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.easymart.presentation.ui.cart.CartViewModel

@Composable
fun SelectPaymentRoute (
    selectPaymentViewModel: SelectPaymentViewModel,
    cartViewModel: CartViewModel,
    onNavigateBack: () -> Unit
){
    val uiState = selectPaymentViewModel.uiState.collectAsState()
    val totalAmount = cartViewModel.total.collectAsState()

    SelectPaymentMethodScreen(
        totalAmount = totalAmount.value,
        state = uiState.value,
        onMethodSelected = { method -> selectPaymentViewModel.selectPaymentMethod(method) },
        onConfirmClick = onNavigateBack
    )
}