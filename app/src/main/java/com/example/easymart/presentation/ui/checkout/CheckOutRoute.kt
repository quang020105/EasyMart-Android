package com.example.easymart.presentation.ui.checkout

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    onNavigateToOnlineProcessing: () -> Unit,
    onNavigateToPaymentFailed: (orderId: Int, reason: String) -> Unit
) {
    //lấy dữ liệu từ cart
    val cartUiState by cartViewModel.uiState.collectAsState()

    //lấy địa chỉ đã chọn
    val selectedAddress by addressViewModel.selectedAddress.collectAsState()

    //trạng thái ui của checkout
    val uiState by checkoutViewModel.uiState.collectAsState()

    //trạng thái ui của selectPayment
    val selectPaymentUiState by paymentViewModel.uiState.collectAsState()

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
                is CheckoutUiEvent.NavigateToPaymentFailed -> {
                    onNavigateToPaymentFailed(event.orderId, event.reason)
                }
                else -> {}
            }
        }
    }


    //Log.d("HomeRoute", "CartViewModel instance hash=${cartViewModel.hashCode()}")
    Log.d("CheckOutRoute", "selectedMethod: ${selectPaymentUiState.selectedMethod}")


    CheckoutScreen(
        address = selectedAddress,
        paymentMethod = selectPaymentUiState.selectedMethod,
        cartItems = cartUiState.items,
        subTotal = cartUiState.subtotal,
        shipping = cartUiState.shipping,
        total = cartUiState.total,
        onAddressClick = { checkoutViewModel.selectAddressClick() },
        onPaymentClick = { checkoutViewModel.selectPaymentClick() },
        isLoading = uiState.isProcessing,
        onConfirmClick = { checkoutViewModel.pay(
            cartItems = cartUiState.items,
            address = selectedAddress,
            paymentMethod = selectPaymentUiState.selectedMethod ?: PaymentMethod.COD
        )}
    )
}