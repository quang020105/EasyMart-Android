package com.example.easymart.presentation.ui.checkout

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.presentation.ui.cart.CartViewModel
import com.example.easymart.presentation.ui.deliveryaddress.AddressViewModel
import com.example.easymart.presentation.ui.payment.SelectPaymentViewModel

@SuppressLint("QueryPermissionsNeeded")
@Composable
fun CheckOutRoute(
    checkoutViewModel: CheckoutViewModel,
    cartViewModel: CartViewModel,
    addressViewModel: AddressViewModel,
    paymentViewModel: SelectPaymentViewModel,
    onNavigateToAddress: () -> Unit,
    onNavigateToPayment: () -> Unit,
    onNavigateToSuccess: () -> Unit,
    onNavigateToOnlineProcessing: (orderCode: Long?, localOrderId: Int?) -> Unit,
    onNavigateToPaymentFailed: (orderId: Int, reason: String) -> Unit
) {
    val context = LocalContext.current

    //lấy dữ liệu từ cart
    val cartUiState by cartViewModel.uiState.collectAsState()

    //lấy địa chỉ đã chọn
    val selectedAddress by addressViewModel.selectedAddress.collectAsState()
    val addressUiState by addressViewModel.uiState.collectAsState()

    //trạng thái ui của checkout
    val uiState by checkoutViewModel.uiState.collectAsState()

    //trạng thái ui của selectPayment
    val selectPaymentUiState by paymentViewModel.uiState.collectAsState()

//     Restore selected items sau khi login thành công và vào checkout
//     Restore khi số lượng items thay đổi (sau khi merge cart hoàn thành)
//    Sử dụng size thay vì toàn bộ items để tránh trigger quá nhiều lần

    LaunchedEffect(cartUiState.items.size, cartUiState.selectedItems.size) {
        // Chỉ restore nếu có items
        if (cartUiState.items.isNotEmpty()) {
            cartViewModel.restoreSelectedItemsAfterLogin()
        }
    }

    LaunchedEffect(Unit) {
        checkoutViewModel.uiEvent.collect { event ->
            when (event) {
                is CheckoutUiEvent.NavigateToSuccess -> {
                    onNavigateToSuccess()
                }

                is CheckoutUiEvent.NavigateToOnlineProcessing -> {
                    val checkoutUrl = event.checkoutUrl
                    if (checkoutUrl.isNullOrBlank()) {
                        Toast.makeText(context, "Checkout URL PayOS không hợp lệ", Toast.LENGTH_SHORT).show()
                        return@collect
                    }

                    // 1) Mở PayOS bằng Chrome Custom Tabs
                    val customTabsIntent = CustomTabsIntent.Builder().build()
                    customTabsIntent.launchUrl(context, Uri.parse(checkoutUrl))

                    // 2) Hiển thị màn Processing ngay khi user được chuyển sang PayOS
                    // Trạng thái cuối cùng sẽ được chốt khi PayOS redirect deeplink về app + webhook cập nhật backend.
                    onNavigateToOnlineProcessing(event.orderCode, event.localOrderId)
                }

                is CheckoutUiEvent.NavigateToSelectAddress -> {
                    onNavigateToAddress()
                }

                is CheckoutUiEvent.NavigateSelectPaymentMethod -> {
                    onNavigateToPayment()
                }

                is CheckoutUiEvent.ShowErrorMessage -> {
                    // TODO: show snackbar/toast nếu bạn muốn
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
        cartItems = cartUiState.selectedItems,
        subTotal = cartUiState.subtotal,
        shipping = cartUiState.shipping,
        total = cartUiState.total,
        onAddressClick = { checkoutViewModel.selectAddressClick() },
        onPaymentClick = { checkoutViewModel.selectPaymentClick() },
        isLoading = uiState.isProcessing,
        isAddressLoading = addressUiState.isLoading,
        onConfirmClick = { checkoutViewModel.pay(
            cartItems = cartUiState.selectedItems,
            address = selectedAddress,
            paymentMethod = selectPaymentUiState.selectedMethod ?: PaymentMethod.COD
        )}
    )
}