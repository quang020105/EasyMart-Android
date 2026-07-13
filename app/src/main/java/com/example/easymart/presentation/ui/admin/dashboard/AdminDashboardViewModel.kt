package com.example.easymart.presentation.ui.admin.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.usecase.product.ImportFakeStoreProductsUseCase
import com.example.easymart.presentation.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    private val importFakeStoreProductsUseCase: ImportFakeStoreProductsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminDashboardUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadSummary()
    }

    fun refresh() {
        loadSummary()
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
                    loadSummary()
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isImportingProducts = false,
                        error = result.message ?: "Không thể import sản phẩm FakeStore"
                    )
                }
                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isImportingProducts = true)
                }
            }
        }
    }

    private fun loadSummary() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            // TODO: hook real admin summary data sources here.
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                orderCount = 128,
                productCount = 54,
                categoryCount = 12,
                revenueToday = 12500000.0
            )
        }
    }
}
