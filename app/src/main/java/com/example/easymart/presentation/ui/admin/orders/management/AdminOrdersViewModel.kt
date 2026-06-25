package com.example.easymart.presentation.ui.admin.orders.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.domain.usecase.order.ObserveAdminOrdersUseCase
import com.example.easymart.presentation.ui.admin.orders.toAdminOrderUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminOrdersViewModel @Inject constructor(
    private val observeAdminOrdersUseCase: ObserveAdminOrdersUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminOrdersUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private var allOrders: List<Order> = emptyList()

    init {
        observeOrders()
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    fun onOrderStatusSelected(status: OrderStatus?) {
        _uiState.update { it.copy(selectedOrderStatus = status) }
        applyFilters()
    }

    fun onPaymentMethodSelected(method: PaymentMethod?) {
        _uiState.update { it.copy(selectedPaymentMethod = method) }
        applyFilters()
    }

    fun onPaymentStatusSelected(status: PaymentStatus?) {
        _uiState.update { it.copy(selectedPaymentStatus = status) }
        applyFilters()
    }

    private fun observeOrders() {
        viewModelScope.launch {
            observeAdminOrdersUseCase()
                .catch { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Khong the tai danh sach don hang"
                        )
                    }
                }
                .collect { orders ->
                    allOrders = orders
                    _uiState.update { it.copy(isLoading = false, errorMessage = null) }
                    applyFilters()
                }
        }
    }

    private fun applyFilters() {
        val state = _uiState.value
        val query = state.searchQuery.trim()
        val filtered = allOrders
            .filter { order ->
                query.isBlank() ||
                    order.orderNumber.contains(query, ignoreCase = true) ||
                    order.remoteId.orEmpty().contains(query, ignoreCase = true) ||
                    order.shippingAddress.name.contains(query, ignoreCase = true) ||
                    order.shippingAddress.phone.contains(query, ignoreCase = true)
            }
            .filter { order -> state.selectedOrderStatus == null || order.status == state.selectedOrderStatus }
            .filter { order -> state.selectedPaymentMethod == null || order.paymentMethod == state.selectedPaymentMethod }
            .filter { order -> state.selectedPaymentStatus == null || order.paymentStatus == state.selectedPaymentStatus }
            .map { it.toAdminOrderUiModel() }

        _uiState.update { it.copy(orders = filtered) }
    }
}
