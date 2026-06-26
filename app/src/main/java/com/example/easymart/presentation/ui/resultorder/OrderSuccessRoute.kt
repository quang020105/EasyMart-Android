package com.example.easymart.presentation.ui.resultorder

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.presentation.ui.checkout.CheckoutViewModel

@Composable
fun OrderSuccessRoute(
    checkoutViewModel: CheckoutViewModel,
    onViewOrderClick: () -> Unit,
    onContinueShoppingClick: () -> Unit, //quay về trang chủ
){
    val uiState = checkoutViewModel.uiState.collectAsState()
    val order = uiState.value.order

    //gọi tự động thay đổi trạng thái đơn hàng
    Log.d("OrderSuccessRoute", "OrderSuccessRoute: ${order?.id}")
    // không dùng nữa vì admin sẽ xử lý đơn hàng, không cần tự động xử lý nữa
//    LaunchedEffect(Unit) {
//        if (order != null) {
//            checkoutViewModel.startAutoProcessOrder()
//        }
//    }

    //xử lý khi nhấn nút back của thiết bị
    BackHandler {
        onContinueShoppingClick()
    }
    OrderSuccessScreen(
        orderId = order?.id ?: -1,
        totalAmount = order?.totalAmount ?: 0L,
        paymentMethod = order?.paymentMethod ?: PaymentMethod.COD,
        onViewOrderClick = onViewOrderClick,
        onContinueShoppingClick = onContinueShoppingClick
    )
}