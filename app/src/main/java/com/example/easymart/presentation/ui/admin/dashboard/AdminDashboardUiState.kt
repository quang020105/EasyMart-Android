package com.example.easymart.presentation.ui.admin.dashboard

import androidx.compose.runtime.Immutable

@Immutable
data class AdminDashboardUiState(
    val isLoading: Boolean = false,
    val orderCount: Int = 0,
    val productCount: Int = 0,
    val categoryCount: Int = 0,
    val revenueToday: Double = 0.0,
    val error: String? = null
)