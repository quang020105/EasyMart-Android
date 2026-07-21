package com.example.easymart.domain.dashboard

import com.example.easymart.domain.model.AdminDashboardMetrics
import com.example.easymart.domain.model.DashboardDataPoint
import com.example.easymart.domain.model.DashboardPeriod
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.domain.model.Product
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class DashboardMetricsCalculator @Inject constructor() {

    fun calculate(
        orders: List<Order>,
        products: List<Product>,
        period: DashboardPeriod,
        nowMillis: Long = System.currentTimeMillis(),
        timeZone: TimeZone = VIETNAM_TIME_ZONE
    ): AdminDashboardMetrics {
        val currentWindow = periodWindow(period, nowMillis, timeZone)
        val previousWindow = PeriodWindow(
            startMillis = shiftDays(currentWindow.startMillis, -period.dayCount, timeZone),
            endExclusiveMillis = currentWindow.startMillis
        )
        val completedOrders = orders.filter { order ->
            order.status == OrderStatus.DELIVERED && order.paymentStatus == PaymentStatus.PAID
        }

        val revenueVnd = completedOrders
            .filter { order -> order.deliveryTimestamp() in currentWindow }
            .sumOf(Order::totalAmount)
        val previousRevenueVnd = completedOrders
            .filter { order -> order.deliveryTimestamp() in previousWindow }
            .sumOf(Order::totalAmount)
        val ordersInPeriod = orders.count { order -> order.createdAt in currentWindow }
        val previousOrdersInPeriod = orders.count { order -> order.createdAt in previousWindow }
        val activeProducts = products.filterNot(Product::isDeleted)

        return AdminDashboardMetrics(
            period = period,
            revenueVnd = revenueVnd,
            previousRevenueVnd = previousRevenueVnd,
            ordersInPeriod = ordersInPeriod,
            previousOrdersInPeriod = previousOrdersInPeriod,
            totalOrderCount = orders.size,
            activeProductCount = activeProducts.size,
            categoryCount = activeProducts
                .map(Product::category)
                .map(String::trim)
                .filter(String::isNotBlank)
                .map(String::lowercase)
                .distinct()
                .size,
            pendingActionCount = orders.count { order ->
                order.status == OrderStatus.CREATED ||
                    order.status == OrderStatus.CANCELLATION_REQUESTED
            },
            dailyRevenue = buildDailySeries(
                window = currentWindow,
                timeZone = timeZone
            ) { dayWindow ->
                completedOrders
                    .filter { order -> order.deliveryTimestamp() in dayWindow }
                    .sumOf(Order::totalAmount)
            },
            dailyOrders = buildDailySeries(
                window = currentWindow,
                timeZone = timeZone
            ) { dayWindow ->
                orders.count { order -> order.createdAt in dayWindow }.toLong()
            }
        )
    }

    //lập danh sách các điểm hàng ngày bằng các cặp labal - value
    private fun buildDailySeries(
        window: PeriodWindow,
        timeZone: TimeZone,
        valueForDay: (PeriodWindow) -> Long
    ): List<DashboardDataPoint> {
        val dayFormat = SimpleDateFormat("dd/MM", Locale.forLanguageTag("vi-VN")).apply {
            this.timeZone = timeZone
        }
        val points = mutableListOf<DashboardDataPoint>()
        var dayStart = window.startMillis
        while (dayStart < window.endExclusiveMillis) {
            val dayEnd = shiftDays(dayStart, 1, timeZone)
            points += DashboardDataPoint(
                label = dayFormat.format(dayStart),
                value = valueForDay(PeriodWindow(dayStart, dayEnd))
            )
            dayStart = dayEnd
        }
        return points
    }

    // lấy khoảng thời gian cho một khonarg thời gian cụ thể
    private fun periodWindow(
        period: DashboardPeriod,
        nowMillis: Long,
        timeZone: TimeZone
    ): PeriodWindow {
        val nextDayStart = shiftDays(startOfDay(nowMillis, timeZone), 1, timeZone)
        return PeriodWindow(
            startMillis = shiftDays(nextDayStart, -period.dayCount, timeZone),
            endExclusiveMillis = nextDayStart
        )
    }

    private fun startOfDay(timeMillis: Long, timeZone: TimeZone): Long {
        return Calendar.getInstance(timeZone).apply {
            timeInMillis = timeMillis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    private fun shiftDays(timeMillis: Long, days: Int, timeZone: TimeZone): Long {
        return Calendar.getInstance(timeZone).apply {
            timeInMillis = timeMillis
            add(Calendar.DATE, days)
        }.timeInMillis
    }

    private fun Order.deliveryTimestamp(): Long = deliveredAt ?: updatedAt

    private operator fun PeriodWindow.contains(timestamp: Long): Boolean =
        timestamp >= startMillis && timestamp < endExclusiveMillis

    private data class PeriodWindow(
        val startMillis: Long,
        val endExclusiveMillis: Long
    )

    private companion object {
        val VIETNAM_TIME_ZONE: TimeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh")
    }
}
