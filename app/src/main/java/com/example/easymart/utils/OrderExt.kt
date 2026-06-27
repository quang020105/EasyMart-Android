package com.example.easymart.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentStatus

fun OrderStatus.toDisplayString(): String {
    return when (this) {
        OrderStatus.CREATED -> "Chờ xử lý"
        OrderStatus.CONFIRMED -> "Đã xác nhận"
        OrderStatus.PACKING -> "Chờ lấy hàng"
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

// màu sắc cho nút hành động dựa trên trạng thái đơn hàng
fun OrderStatus.toActionColor(): Color {
    return when (this) {
        OrderStatus.CREATED, OrderStatus.CONFIRMED -> Color(0xFFD32F2F)
        OrderStatus.PACKING, OrderStatus.SHIPPING -> Color(0xFF1976D2)
        OrderStatus.DELIVERED, OrderStatus.CANCELLED -> Color(0xFF2E7D32)
    }
}


@Immutable
data class ChipColorScheme(
    val containerColor: Color,
    val contentColor: Color,
    val borderColor: Color
)

fun OrderStatus.colorScheme(): ChipColorScheme {
    return when (this) {
        OrderStatus.CREATED -> ChipColorScheme(
            containerColor = Color(0xFFFFF7ED),
            contentColor = Color(0xFFEA580C),
            borderColor = Color(0xFFFED7AA)
        )

        OrderStatus.CONFIRMED -> ChipColorScheme(
            containerColor = Color(0xFFEFF6FF),
            contentColor = Color(0xFF2563EB),
            borderColor = Color(0xFFBFDBFE)
        )

        OrderStatus.PACKING -> ChipColorScheme(
            containerColor = Color(0xFFF5F3FF),
            contentColor = Color(0xFF7C3AED),
            borderColor = Color(0xFFC4B5FD)
        )

        OrderStatus.SHIPPING -> ChipColorScheme(
            containerColor = Color(0xFFECFEFF),
            contentColor = Color(0xFF0891B2),
            borderColor = Color(0xFFA5F3FC)
        )

        OrderStatus.DELIVERED -> ChipColorScheme(
            containerColor = Color(0xFFF0FDF4),
            contentColor = Color(0xFF16A34A),
            borderColor = Color(0xFFBBF7D0)
        )

        OrderStatus.CANCELLED -> ChipColorScheme(
            containerColor = Color(0xFFFEF2F2),
            contentColor = Color(0xFFDC2626),
            borderColor = Color(0xFFFECACA)
        )
    }
}


// màu sắc cho nút hành động dựa trên trạng thái thanh toán

fun PaymentStatus.colorScheme(): ChipColorScheme {
    return when (this) {
        PaymentStatus.UNPAID -> ChipColorScheme(
            containerColor = Color(0xFFF8FAFC),
            contentColor = Color(0xFF64748B),
            borderColor = Color(0xFFCBD5E1)
        )

        PaymentStatus.PENDING -> ChipColorScheme(
            containerColor = Color(0xFFEFF6FF),
            contentColor = Color(0xFF2563EB),
            borderColor = Color(0xFFBFDBFE)
        )

        PaymentStatus.PROCESSING -> ChipColorScheme(
            containerColor = Color(0xFFFFF7ED),
            contentColor = Color(0xFFEA580C),
            borderColor = Color(0xFFFED7AA)
        )

        PaymentStatus.PAID -> ChipColorScheme(
            containerColor = Color(0xFFF0FDF4),
            contentColor = Color(0xFF16A34A),
            borderColor = Color(0xFFBBF7D0)
        )

        PaymentStatus.FAILED -> ChipColorScheme(
            containerColor = Color(0xFFFEF2F2),
            contentColor = Color(0xFFDC2626),
            borderColor = Color(0xFFFECACA)
        )

        PaymentStatus.CANCELLED -> ChipColorScheme(
            containerColor = Color(0xFFF1F5F9),
            contentColor = Color(0xFF475569),
            borderColor = Color(0xFFCBD5E1)
        )

        PaymentStatus.REFUND_REQUIRED -> ChipColorScheme(
            containerColor = Color(0xFFFFFBEB),
            contentColor = Color(0xFFD97706),
            borderColor = Color(0xFFFDE68A)
        )

        PaymentStatus.REFUNDING -> ChipColorScheme(
            containerColor = Color(0xFFF5F3FF),
            contentColor = Color(0xFF7C3AED),
            borderColor = Color(0xDDD6FE)
        )

        PaymentStatus.REFUNDED -> ChipColorScheme(
            containerColor = Color(0xFFECFDF5),
            contentColor = Color(0xFF059669),
            borderColor = Color(0xFFA7F3D0)
        )
    }
}

// trạng thái thanh toán
fun PaymentStatus.label(): String {
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

@Composable
fun PaymentMethod.colorScheme(): ChipColorScheme {
    return ChipColorScheme(
        containerColor = Color(0xFFEFF6FF),
        contentColor = Color(0xFF2563EB),
        borderColor = Color(0xFFBFDBFE)
    )
}

// action chuyển trạng đơn hàng cho admin
fun OrderStatus.toAdminPrimaryActionString(): String? {
    return when (this) {
        OrderStatus.CREATED -> "Xác nhận đơn"
        OrderStatus.CONFIRMED -> "Chuyển sang chờ lấy hàng"
        OrderStatus.PACKING -> "Chuyển sang đang giao"
        OrderStatus.SHIPPING -> "Xác nhận đã giao"
        OrderStatus.DELIVERED,
        OrderStatus.CANCELLED -> null
    }
}

fun OrderStatus.canAdminCancel(): Boolean {
    return when (this) {
        OrderStatus.CREATED,
        OrderStatus.CONFIRMED,
        OrderStatus.PACKING -> true

        OrderStatus.SHIPPING,
        OrderStatus.DELIVERED,
        OrderStatus.CANCELLED -> false
    }
}

fun OrderStatus.hasAdminAction(): Boolean {
    return toAdminPrimaryActionString() != null || canAdminCancel()
}

fun PaymentStatus.isPaid(): Boolean {
    return this == PaymentStatus.PAID
}


fun PaymentMethod.toShortLabel(): String {
    return when (this) {
        PaymentMethod.COD -> "COD"
        PaymentMethod.ONLINE_GATEWAY -> "Online"
        PaymentMethod.WALLET -> "Ví điện tử"
    }
}

