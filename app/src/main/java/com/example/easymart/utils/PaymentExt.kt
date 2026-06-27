package com.example.easymart.utils

import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentStatus

fun PaymentMethod.toDisplayString(): String {
    return when (this) {
        PaymentMethod.COD -> "Thanh toán khi nhận hàng"
        PaymentMethod.ONLINE_GATEWAY -> "Thanh toán trực tuyến"
        PaymentMethod.WALLET -> "Ví điện tử"
    }
}

//chuyển trạng thái thanh toán sang chuỗi hiển thị
fun PaymentStatus.toDisplayString(): String {
    return when (this) {
        PaymentStatus.UNPAID -> "Chưa thanh toán"
        PaymentStatus.PENDING -> "Đang chờ thanh toán"
        PaymentStatus.PROCESSING -> "Đang xử lý"
        PaymentStatus.PAID -> "Đã thanh toán"
        PaymentStatus.FAILED -> "Thanh toán thất bại"
        PaymentStatus.CANCELLED -> "Đã hủy thanh toán"
        PaymentStatus.REFUND_REQUIRED -> "Cần hoàn tiền"
        PaymentStatus.REFUNDING -> "Đang hoàn tiền"
        PaymentStatus.REFUNDED -> "Đã hoàn tiền"
    }
}