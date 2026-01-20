package com.example.easymart.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import com.example.easymart.domain.model.OrderStatus

fun OrderStatus.toDisplayString(): String {
    return when (this) {
        OrderStatus.CREATED -> "Chờ xử lý"
        OrderStatus.CONFIRMED -> "Đã xác nhận"
        OrderStatus.PROCESSING -> "Chờ lấy hàng"
        OrderStatus.SHIPPING -> "Đang giao"
        OrderStatus.DELIVERED -> "Đã giao"
        OrderStatus.CANCELLED -> "Đã hủy"
    }
}

fun OrderStatus.toActionString(): String? {
    return when (this) {
        OrderStatus.CREATED, OrderStatus.CONFIRMED -> "Hủy đơn"
        OrderStatus.SHIPPING -> "Theo dõi đơn hàng"
        OrderStatus.DELIVERED, OrderStatus.CANCELLED -> "Mua lại"
        else -> null
    }
}

fun OrderStatus.toActionColor(): Color {
    return when (this) {
        OrderStatus.CREATED, OrderStatus.CONFIRMED -> Color(0xFFD32F2F)
        OrderStatus.PROCESSING, OrderStatus.SHIPPING -> Color(0xFF1976D2)
        OrderStatus.DELIVERED, OrderStatus.CANCELLED -> Color(0xFF2E7D32)
    }
}