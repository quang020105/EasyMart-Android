package com.example.easymart.presentation.ui.payment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.easymart.presentation.ui.cart.CartViewModel

@Composable
fun SelectPaymentRoute (
    selectPaymentViewModel: SelectPaymentViewModel,
    cartViewModel: CartViewModel,
    onNavigateBack: () -> Unit
){
    val uiState by selectPaymentViewModel.uiState.collectAsState()
    val cartUiState by cartViewModel.uiState.collectAsState()

    SelectPaymentMethodScreen(
        totalAmount = cartUiState.total,
        uiState = uiState,
        onMethodSelected = { method -> selectPaymentViewModel.selectPaymentMethod(method) },
        onConfirmClick = onNavigateBack
    )
}