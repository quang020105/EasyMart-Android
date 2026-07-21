package com.example.easymart.presentation.ui.admin.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.model.DashboardPeriod
import com.example.easymart.domain.usecase.dashboard.ObserveAdminDashboardMetricsUseCase
import com.example.easymart.domain.usecase.product.ImportFakeStoreProductsUseCase
import com.example.easymart.presentation.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    private val observeAdminDashboardMetricsUseCase: ObserveAdminDashboardMetricsUseCase,
    private val importFakeStoreProductsUseCase: ImportFakeStoreProductsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminDashboardUiState())
    val uiState = _uiState.asStateFlow()

    private var dashboardJob: Job? = null

    init {
        observeDashboard()
    }

    fun refresh() {
        observeDashboard()
    }

    fun selectPeriod(period: DashboardPeriod) {
        if (_uiState.value.period == period) return
        _uiState.value = _uiState.value.copy(period = period)
        observeDashboard()
    }

    fun importFakeStoreProducts() {
        if (_uiState.value.isImportingProducts) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isImportingProducts = true,
                importMessage = null,
                error = null
            )

            when (val result = importFakeStoreProductsUseCase()) {
                is Resource.Success -> {
                    val count = result.data
                    _uiState.value = _uiState.value.copy(
                        isImportingProducts = false,
                        importMessage = if (count > 0) {
                            "Đã import $count sản phẩm FakeStore lên Firebase"
                        } else {
                            "FakeStore đã được đồng bộ, không có sản phẩm mới"
                        }
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isImportingProducts = false,
                        error = result.message ?: "Không thể import sản phẩm FakeStore"
                    )
                }
                Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isImportingProducts = true)
                }
            }
        }
    }

    private fun observeDashboard() {
        dashboardJob?.cancel()
        dashboardJob = viewModelScope.launch {
            observeAdminDashboardMetricsUseCase(_uiState.value.period)
                .onStart {
                    _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                }
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            val metrics = result.data
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                revenueVnd = metrics.revenueVnd,
                                previousRevenueVnd = metrics.previousRevenueVnd,
                                ordersInPeriod = metrics.ordersInPeriod,
                                previousOrdersInPeriod = metrics.previousOrdersInPeriod,
                                totalOrderCount = metrics.totalOrderCount,
                                productCount = metrics.activeProductCount,
                                categoryCount = metrics.categoryCount,
                                pendingActionCount = metrics.pendingActionCount,
                                dailyRevenue = metrics.dailyRevenue,
                                dailyOrders = metrics.dailyOrders,
                                error = null
                            )
                        }
                        is Resource.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = result.message ?: "Không thể tải thống kê quản trị"
                            )
                        }
                        Resource.Loading -> {
                            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                        }
                    }
                }
        }
    }
}
