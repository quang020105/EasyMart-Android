package com.example.easymart.presentation.ui.checkout

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.presentation.ui.cart.CartViewModel
import com.example.easymart.presentation.ui.deliveryaddress.AddressViewModel
import com.example.easymart.presentation.ui.payment.SelectPaymentViewModel

@Composable
fun CheckOutRoute(
    checkoutViewModel: CheckoutViewModel,
    cartViewModel: CartViewModel,
    addressViewModel: AddressViewModel,
    paymentViewModel: SelectPaymentViewModel,
    onNavigateToAddress: () -> Unit,
    onNavigateToPayment: () -> Unit,
    onNavigateToSuccess: () -> Unit,
    onNavigateToOnlineProcessing: () -> Unit
) {
    //lấy dữ liệu từ cart
    val selectedItems = cartViewModel.selectedItems.collectAsState(initial = emptyList())
    val subTotal = cartViewModel.subtotal.collectAsState()
    val shipping = cartViewModel.shipping.collectAsState()
    val total = cartViewModel.total.collectAsState()
    //lấy địa chỉ đã chọn
    val selectedAddress = addressViewModel.selectedAddress.collectAsState()
    //trạng thái ui của checkout
    val uiState = checkoutViewModel.uiState.collectAsState()
    //trạng thái ui của selectPayment
    val selectPaymentUiState = paymentViewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        checkoutViewModel.uiEvent.collect { event ->
            when (event) {
                is CheckoutUiEvent.NavigateToSuccess -> {
                    onNavigateToSuccess()
                }

                is CheckoutUiEvent.NavigateToOnlineProcessing -> {
                    onNavigateToOnlineProcessing()
                }

                is CheckoutUiEvent.NavigateToSelectAddress -> {
                    onNavigateToAddress()
                }

                is CheckoutUiEvent.NavigateSelectPaymentMethod -> {
                    onNavigateToPayment()
                }

                is CheckoutUiEvent.ShowErrorMessage -> {
                    //todo
                }
                else -> {}
            }
        }
    }


    Log.d("HomeRoute", "CartViewModel instance hash=${cartViewModel.hashCode()}")

    CheckoutScreen(
        address = selectedAddress.value,
        paymentMethod = selectPaymentUiState.value.selectedMethod,
        cartItems = selectedItems.value,
        subTotal = subTotal.value,
        shipping = shipping.value,
        total = total.value,
        onAddressClick = { checkoutViewModel.selectAddressClick() },
        onPaymentClick = { checkoutViewModel.selectPaymentClick() },
        isLoading = uiState.value.isProcessing,
        onConfirmClick = { checkoutViewModel.pay(
            cartItems = selectedItems.value,
            address = selectedAddress.value,
            paymentMethod = selectPaymentUiState.value.selectedMethod ?: PaymentMethod.COD
        )}
    )
}