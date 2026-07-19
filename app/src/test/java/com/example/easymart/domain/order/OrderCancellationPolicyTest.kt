package com.example.easymart.domain.order

import com.example.easymart.domain.model.Address
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentStatus
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class OrderCancellationPolicyTest {

    @Test
    fun `customer can cancel an unpaid created order immediately`() {
        val order = order(status = OrderStatus.CREATED, paymentStatus = PaymentStatus.UNPAID)

        assertThat(OrderCancellationPolicy.canCustomerCancelImmediately(order)).isTrue()
        assertThat(OrderCancellationPolicy.canCustomerRequestCancellation(order)).isFalse()
    }

    @Test
    fun `customer cannot request cancellation while online payment is pending`() {
        val order = order(status = OrderStatus.CREATED, paymentStatus = PaymentStatus.PENDING)

        assertThat(OrderCancellationPolicy.canCustomerRequestCancellation(order)).isFalse()
    }

    @Test
    fun `paid cancellation requires refund and restores deducted stock once`() {
        val order = order(
            status = OrderStatus.CANCELLATION_REQUESTED,
            paymentStatus = PaymentStatus.PAID,
            stockDeducted = true
        )

        assertThat(OrderCancellationPolicy.canAdminApproveCancellation(order)).isTrue()
        assertThat(OrderCancellationPolicy.paymentStatusAfterCancellation(order.paymentStatus))
            .isEqualTo(PaymentStatus.REFUND_REQUIRED)
        assertThat(OrderCancellationPolicy.shouldRestoreStock(order)).isTrue()
        assertThat(OrderCancellationPolicy.shouldRestoreStock(order.copy(stockRestored = true))).isFalse()
    }

    private fun order(
        status: OrderStatus,
        paymentStatus: PaymentStatus,
        stockDeducted: Boolean = false
    ) = Order(
        userId = "user-1",
        orderNumber = "ORD-1",
        totalAmount = 100_000L,
        status = status,
        paymentStatus = paymentStatus,
        shippingAddress = Address(),
        stockDeducted = stockDeducted
    )
}
