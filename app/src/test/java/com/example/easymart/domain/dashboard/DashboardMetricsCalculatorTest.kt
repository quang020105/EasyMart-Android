package com.example.easymart.domain.dashboard

import com.example.easymart.domain.model.Address
import com.example.easymart.domain.model.DashboardPeriod
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.domain.model.Product
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.util.Calendar
import java.util.TimeZone

class DashboardMetricsCalculatorTest {

    private val timeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh")
    private val calculator = DashboardMetricsCalculator()
    private val noonToday = localTime(day = 21, hour = 12)

    @Test
    fun `today revenue counts only delivered paid orders`() {
        val metrics = calculator.calculate(
            orders = listOf(
                order(
                    id = 1,
                    totalAmount = 100_000L,
                    status = OrderStatus.DELIVERED,
                    paymentStatus = PaymentStatus.PAID,
                    createdAt = noonToday,
                    deliveredAt = noonToday
                ),
                order(
                    id = 2,
                    totalAmount = 80_000L,
                    status = OrderStatus.CANCELLED,
                    paymentStatus = PaymentStatus.REFUND_REQUIRED,
                    createdAt = noonToday,
                    deliveredAt = noonToday
                ),
                order(
                    id = 3,
                    totalAmount = 50_000L,
                    status = OrderStatus.DELIVERED,
                    paymentStatus = PaymentStatus.PAID,
                    createdAt = localTime(day = 20, hour = 10),
                    deliveredAt = localTime(day = 20, hour = 10)
                )
            ),
            products = emptyList(),
            period = DashboardPeriod.TODAY,
            nowMillis = noonToday,
            timeZone = timeZone
        )

        assertThat(metrics.revenueVnd).isEqualTo(100_000L)
        assertThat(metrics.previousRevenueVnd).isEqualTo(50_000L)
        assertThat(metrics.ordersInPeriod).isEqualTo(2)
    }

    @Test
    fun `product and pending counts exclude deleted products and classify actionable orders`() {
        val metrics = calculator.calculate(
            orders = listOf(
                order(id = 1, status = OrderStatus.CREATED),
                order(id = 2, status = OrderStatus.CANCELLATION_REQUESTED),
                order(id = 3, status = OrderStatus.SHIPPING)
            ),
            products = listOf(
                product(id = 1, category = "Đồ uống"),
                product(id = 2, category = "đồ uống"),
                product(id = 3, category = "Sách"),
                product(id = 4, category = "Ẩn", isDeleted = true)
            ),
            period = DashboardPeriod.TODAY,
            nowMillis = noonToday,
            timeZone = timeZone
        )

        assertThat(metrics.activeProductCount).isEqualTo(3)
        assertThat(metrics.categoryCount).isEqualTo(2)
        assertThat(metrics.pendingActionCount).isEqualTo(2)
    }

    private fun order(
        id: Int,
        totalAmount: Long = 0L,
        status: OrderStatus,
        paymentStatus: PaymentStatus = PaymentStatus.UNPAID,
        createdAt: Long = noonToday,
        deliveredAt: Long? = null
    ) = Order(
        id = id,
        userId = "user-$id",
        orderNumber = "ORD-$id",
        totalAmount = totalAmount,
        status = status,
        paymentStatus = paymentStatus,
        shippingAddress = Address(),
        createdAt = createdAt,
        updatedAt = createdAt,
        deliveredAt = deliveredAt
    )

    private fun product(id: Int, category: String, isDeleted: Boolean = false) = Product(
        id = id,
        name = "Sản phẩm $id",
        description = null,
        priceVnd = 10_000L,
        imageUrl = "",
        category = category,
        isDeleted = isDeleted
    )

    private fun localTime(day: Int, hour: Int): Long {
        return Calendar.getInstance(timeZone).apply {
            set(2026, Calendar.JULY, day, hour, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }
}
