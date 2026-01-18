package com.example.easymart.utils

import com.example.easymart.domain.model.OrderStatus

fun Double.toVNDString(): String {
    val vnd = this * 26333
    return "%,.0f ₫".format(vnd).replace(',', '.')
}

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