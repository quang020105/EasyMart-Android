package com.example.easymart.presentation.ui.admin.orders.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.domain.usecase.order.GetAdminOrderDetailUseCase
import com.example.easymart.domain.usecase.order.UpdateAdminOrderStatusUseCase
import com.example.easymart.presentation.ui.admin.orders.toAdminOrderDetailUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminOrderDetailViewModel @Inject constructor(
    private val getAdminOrderDetailUseCase: GetAdminOrderDetailUseCase,
    private val updateAdminOrderStatusUseCase: UpdateAdminOrderStatusUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminOrderDetailUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private var remoteId: String? = null

    fun loadOrder(remoteId: String) {
        this.remoteId = remoteId
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching { getAdminOrderDetailUseCase(remoteId) }
                .onSuccess { order ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            order = order?.toAdminOrderDetailUiModel(),
                            errorMessage = if (order == null) "Không tìm thấy đơn hàng" else null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            order = null,
                            errorMessage = error.message ?: "Khong the tai chi tiet don hang"
                        )
                    }
                }
        }
    }

    fun confirmOrder() = updateStatus(OrderStatus.CONFIRMED)

    fun cancelOrder() = updateStatus(OrderStatus.CANCELLED, PaymentStatus.FAILED)

    fun moveToProcessing() = updateStatus(OrderStatus.PROCESSING)

    fun moveToShipping() = updateStatus(OrderStatus.SHIPPING)

    fun confirmDelivered() {
        val paymentStatus = when (_uiState.value.order?.paymentStatus) {
            PaymentStatus.SUCCESS -> null
            else -> PaymentStatus.SUCCESS
        }
        updateStatus(OrderStatus.DELIVERED, paymentStatus)
    }

    private fun updateStatus(orderStatus: OrderStatus, paymentStatus: PaymentStatus? = null) {
        val id = remoteId ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isActionLoading = true, errorMessage = null) }
            runCatching {
                updateAdminOrderStatusUseCase(id, orderStatus, paymentStatus)
                getAdminOrderDetailUseCase(id)
            }.onSuccess { order ->
                _uiState.update {
                    it.copy(
                        isActionLoading = false,
                        order = order?.toAdminOrderDetailUiModel(),
                        errorMessage = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isActionLoading = false,
                        errorMessage = error.message ?: "Thay đổi trạng thái đơn hàng không thành công. Vui lòng thử lại."
                    )
                }
            }
        }
    }
}
