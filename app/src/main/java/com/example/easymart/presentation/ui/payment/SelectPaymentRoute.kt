package com.example.easymart.presentation.ui.payment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.easymart.presentation.ui.cart.CartViewModel
import com.example.easymart.presentation.ui.checkout.CheckoutViewModel

@Composable
fun SelectPaymentRoute (
    selectPaymentViewModel: SelectPaymentViewModel,
    cartViewModel: CartViewModel,
    checkoutViewModel: CheckoutViewModel,
    onNavigateBack: () -> Unit
){
    val uiState by selectPaymentViewModel.uiState.collectAsState()
    val cartUiState by cartViewModel.uiState.collectAsState()
    val checkoutUiState by checkoutViewModel.uiState.collectAsState()

    val quickShipping = checkoutUiState.quickOrderItems.sumOf { it.quantity * (15000.0 / 26333) }
    val quickTotal = checkoutUiState.quickOrderItems.sumOf { it.totalPrice } + quickShipping
    val totalAmount = if (checkoutUiState.isQuickOrderActive) {
        quickTotal
    } else {
        cartUiState.total
    }

    SelectPaymentMethodScreen(
        totalAmount = totalAmount,
        uiState = uiState,
        onMethodSelected = { method -> selectPaymentViewModel.selectPaymentMethod(method) },
        onConfirmClick = onNavigateBack
    )
}