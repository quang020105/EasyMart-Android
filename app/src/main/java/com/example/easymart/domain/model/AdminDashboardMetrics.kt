package com.example.easymart.domain.model

data class AdminDashboardMetrics(
    val period: DashboardPeriod,
    val revenueVnd: Long,
    val previousRevenueVnd: Long,
    val ordersInPeriod: Int,
    val previousOrdersInPeriod: Int,
    val totalOrderCount: Int,
    val activeProductCount: Int,
    val categoryCount: Int,
    val pendingActionCount: Int,
    val dailyRevenue: List<DashboardDataPoint>,
    val dailyOrders: List<DashboardDataPoint>
)

data class DashboardDataPoint(
    val label: String,
    val value: Long
)

enum class DashboardPeriod(val dayCount: Int, val label: String) {
    TODAY(dayCount = 1, label = "Hôm nay"),
    LAST_7_DAYS(dayCount = 7, label = "7 ngày qua"),
    LAST_30_DAYS(dayCount = 30, label = "30 ngày qua")
}
