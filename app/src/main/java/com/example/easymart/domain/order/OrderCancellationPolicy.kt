package com.example.easymart.domain.order

import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentStatus

object OrderCancellationPolicy {
    // kiểm tra xem khách hàng có thể hủy đơn hàng ngay lập tức không
    fun canCustomerCancelImmediately(order: Order): Boolean {
        return order.status == OrderStatus.CREATED &&
            order.paymentStatus !in setOf(PaymentStatus.PENDING, PaymentStatus.PROCESSING)
    }

    // kiểm tra xem khách hàng có thể yêu cầu hủy đơn hàng không
    fun canCustomerRequestCancellation(order: Order): Boolean {
        return order.status == OrderStatus.CONFIRMED &&
            order.paymentStatus !in setOf(PaymentStatus.PENDING, PaymentStatus.PROCESSING)
    }

    // kiểm tra xem quản trị viên có thể phê duyệt yêu cầu hủy đơn hàng không
    fun canAdminApproveCancellation(order: Order): Boolean {
        return order.status in setOf(
            OrderStatus.CREATED,
            OrderStatus.CONFIRMED,
            OrderStatus.PACKING,
            OrderStatus.CANCELLATION_REQUESTED
        ) && order.paymentStatus !in setOf(PaymentStatus.PENDING, PaymentStatus.PROCESSING)
    }

    // chuyển trạng thái thanh toán sau khi hủy đơn hàng
    fun paymentStatusAfterCancellation(current: PaymentStatus): PaymentStatus {
        return when (current) {
            PaymentStatus.PAID -> PaymentStatus.REFUND_REQUIRED
            PaymentStatus.UNPAID -> PaymentStatus.CANCELLED
            PaymentStatus.FAILED -> PaymentStatus.FAILED
            PaymentStatus.CANCELLED -> PaymentStatus.CANCELLED
            PaymentStatus.REFUND_REQUIRED,
            PaymentStatus.REFUNDING,
            PaymentStatus.REFUNDED -> current
            PaymentStatus.PENDING,
            PaymentStatus.PROCESSING -> error("Pending payments must be resolved before cancellation")
        }
    }

    fun shouldRestoreStock(order: Order): Boolean =
        order.stockDeducted && !order.stockRestored
}
