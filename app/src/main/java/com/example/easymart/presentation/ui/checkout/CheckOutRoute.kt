package com.example.easymart.presentation.ui.checkout

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.easymart.presentation.ui.cart.CartViewModel
import com.example.easymart.presentation.ui.deliveryaddress.AddressViewModel
import com.example.easymart.presentation.ui.payment.PaymentViewModel

@Composable
fun CheckOutRoute(
    cartViewModel: CartViewModel,
    addressViewModel: AddressViewModel,
    paymentViewModel: PaymentViewModel,
    onNavigateToAddress: () -> Unit,
    onNavigateToPayment: () -> Unit,
){
    val selectedItems = cartViewModel.selectedItems.collectAsState(initial = emptyList())
    val subTotal = cartViewModel.subtotal.collectAsState()
    val shipping = cartViewModel.shipping.collectAsState()
    val total = cartViewModel.total.collectAsState()
    val selectedAddress = addressViewModel.selectedAddress.collectAsState()
    val paymentMethod = paymentViewModel.uiState.collectAsState()


    Log.d("HomeRoute", "CartViewModel instance hash=${cartViewModel.hashCode()}")

    CheckoutScreen(
        address = selectedAddress.value,
        paymentMethod = paymentMethod.value.selectedMethod,
        cartItems = selectedItems.value,
        subTotal = subTotal.value,
        shipping = shipping.value,
        total = total.value,
        onAddressClick = onNavigateToAddress,
        onPaymentClick = onNavigateToPayment
    )
}