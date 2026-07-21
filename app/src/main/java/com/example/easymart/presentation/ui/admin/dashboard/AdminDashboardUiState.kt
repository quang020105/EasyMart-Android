package com.example.easymart.presentation.ui.admin.dashboard

import androidx.compose.runtime.Immutable
import com.example.easymart.domain.model.DashboardDataPoint
import com.example.easymart.domain.model.DashboardPeriod

@Immutable
data class AdminDashboardUiState(
    val isLoading: Boolean = true,
    val period: DashboardPeriod = DashboardPeriod.TODAY,
    val revenueVnd: Long = 0L,
    val previousRevenueVnd: Long = 0L,
    val ordersInPeriod: Int = 0,
    val previousOrdersInPeriod: Int = 0,
    val totalOrderCount: Int = 0,
    val productCount: Int = 0,
    val categoryCount: Int = 0,
    val pendingActionCount: Int = 0,
    val dailyRevenue: List<DashboardDataPoint> = emptyList(),
    val dailyOrders: List<DashboardDataPoint> = emptyList(),
    val error: String? = null,
    val isImportingProducts: Boolean = false,
    val importMessage: String? = null
)
