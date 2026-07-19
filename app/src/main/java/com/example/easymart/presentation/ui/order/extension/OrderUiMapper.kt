package com.example.easymart.presentation.ui.order.extension

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.easymart.domain.model.OrderStatus

@Composable
fun OrderStatus.toColor(): Color {
    return when (this) {
        OrderStatus.SHIPPING -> MaterialTheme.colorScheme.secondary
        OrderStatus.CONFIRMED -> MaterialTheme.colorScheme.primary
        OrderStatus.CANCELLED -> MaterialTheme.colorScheme.error
        OrderStatus.DELIVERED -> Color(0xFF2E7D32)
        else -> MaterialTheme.colorScheme.tertiary
    }
}

fun OrderStatus.toPrimaryActionText(): String? {
    return when (this) {
        OrderStatus.SHIPPING -> "Theo dõi đơn hàng"
        OrderStatus.DELIVERED,
        OrderStatus.CANCELLED -> "Mua lại"
        OrderStatus.CREATED,
        OrderStatus.CONFIRMED,
        OrderStatus.PACKING,
        OrderStatus.CANCELLATION_REQUESTED -> null
    }
}
