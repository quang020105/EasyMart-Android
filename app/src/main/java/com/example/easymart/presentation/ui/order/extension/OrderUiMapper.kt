package com.example.easymart.presentation.ui.order.extension

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentStatus

@Composable
fun OrderStatus.toColor(): Color {
    return when (this) {
        OrderStatus.SHIPPING -> MaterialTheme.colorScheme.secondary
        OrderStatus.CONFIRMED -> MaterialTheme.colorScheme.primary
        OrderStatus.CANCELLED -> MaterialTheme.colorScheme.error
        OrderStatus.DELIVERED ->  Color(0xFF2E7D32)
        else -> MaterialTheme.colorScheme.tertiary
    }
}

fun OrderStatus.toPrimaryActionText(
    paymentMethod: PaymentMethod,
    paymentStatus: PaymentStatus
): String? {
    return when (this) {
        OrderStatus.CREATED -> {
            when {
                paymentMethod == PaymentMethod.COD -> "Hủy đơn"

                paymentMethod == PaymentMethod.ONLINE_GATEWAY &&
                        paymentStatus == PaymentStatus.PAID -> "Yêu cầu hủy đơn & hoàn tiền"

                paymentMethod == PaymentMethod.ONLINE_GATEWAY -> "Hủy đơn"

                else -> null
            }
        }

        OrderStatus.CONFIRMED,
        OrderStatus.PACKING -> {
            when {
                paymentMethod == PaymentMethod.COD -> "Yêu cầu hủy đơn"

                paymentMethod == PaymentMethod.ONLINE_GATEWAY &&
                        paymentStatus == PaymentStatus.PAID -> "Yêu cầu hủy đơn & hoàn tiền"

                paymentMethod == PaymentMethod.ONLINE_GATEWAY -> "Yêu cầu hủy đơn"

                else -> null
            }
        }

        OrderStatus.SHIPPING -> "Theo dõi đơn hàng"

        OrderStatus.DELIVERED -> "Mua lại"

        OrderStatus.CANCELLED -> "Mua lại"
    }
}