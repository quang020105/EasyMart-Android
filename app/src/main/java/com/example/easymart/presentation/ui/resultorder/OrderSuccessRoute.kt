package com.example.easymart.presentation.ui.resultorder

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.example.easymart.domain.model.PaymentMethod

@Composable
fun OrderSuccessRoute(
    orderId: Int,
    totalAmount: Long,
    paymentMethod: PaymentMethod,
    onViewOrderClick: () -> Unit,
    onContinueShoppingClick: () -> Unit, //quay về trang chủ
){
    //xử lý khi nhấn nút back của thiết bị
    BackHandler {
        onContinueShoppingClick()
    }
    OrderSuccessScreen(
        orderId = orderId,
        totalAmount = totalAmount,
        paymentMethod = paymentMethod,
        onViewOrderClick = onViewOrderClick,
        onContinueShoppingClick = onContinueShoppingClick
    )
}