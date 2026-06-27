package com.example.easymart.presentation.ui.admin.orders.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.domain.usecase.order.GetAdminOrderDetailUseCase
import com.example.easymart.domain.usecase.order.UpdateAdminOrderStatusUseCase
import com.example.easymart.presentation.ui.admin.orders.toAdminOrderDetailUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
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

    private val _uiEvent = Channel<AdminOrderDetailUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

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
                            errorMessage = error.message ?: "Không thể xem chi tiết đơn hàng"
                        )
                    }
                }
        }
    }

    fun confirmOrder() = requestStatusChange(
        orderStatus = OrderStatus.CONFIRMED,
        title = "Xác nhận đơn hàng",
        message = "Bạn có chắc muốn xác nhận đơn hàng này?",
        confirmText = "Xác nhận"
    )

    fun cancelOrder() = requestStatusChange(
        orderStatus = OrderStatus.CANCELLED,
        paymentStatus = PaymentStatus.FAILED,
        title = "Hủy đơn hàng",
        message = "Bạn có chắc muốn hủy đơn hàng này? Thao tác này sẽ cập nhật trạng thái đơn và thanh toán.",
        confirmText = "Hủy đơn",
        isDanger = true
    )

    fun moveToProcessing() = requestStatusChange(
        orderStatus = OrderStatus.PROCESSING,
        title = "Chuyển sang chờ lấy hàng",
        message = "Bạn có chắc muốn chuyển đơn hàng sang trạng thái chờ lấy hàng?",
        confirmText = "Chuyển trạng thái"
    )

    fun moveToShipping() = requestStatusChange(
        orderStatus = OrderStatus.SHIPPING,
        title = "Chuyển sang đang giao",
        message = "Bạn có chắc muốn chuyển đơn hàng sang trạng thái đang giao?",
        confirmText = "Chuyển trạng thái"
    )

    fun confirmDelivered() {
        val paymentStatus = when (_uiState.value.order?.paymentStatus) {
            PaymentStatus.SUCCESS -> null
            else -> PaymentStatus.SUCCESS
        }
        requestStatusChange(
            orderStatus = OrderStatus.DELIVERED,
            paymentStatus = paymentStatus,
            title = "Xác nhận đã giao",
            message = "Bạn có chắc đơn hàng này đã được giao thành công?",
            confirmText = "Xác nhận"
        )
    }

    fun dismissConfirmation() {
        _uiState.update { it.copy(pendingConfirmation = null) }
    }

    // xác nhận thay đổi trạng thái đơn hàng
    fun confirmPendingAction() {
        val confirmation = _uiState.value.pendingConfirmation ?: return
        updateStatus(
            orderStatus = confirmation.orderStatus,
            paymentStatus = confirmation.paymentStatus
        )
    }

    private fun requestStatusChange(
        orderStatus: OrderStatus,
        title: String,
        message: String,
        confirmText: String,
        paymentStatus: PaymentStatus? = null,
        isDanger: Boolean = false
    ) {
        if (_uiState.value.isActionLoading) return
        _uiState.update {
            it.copy(
                pendingConfirmation = AdminOrderConfirmation(
                    title = title,
                    message = message,
                    confirmText = confirmText,
                    isDanger = isDanger,
                    orderStatus = orderStatus,
                    paymentStatus = paymentStatus
                )
            )
        }
    }

    // update thật
    private fun updateStatus(orderStatus: OrderStatus, paymentStatus: PaymentStatus? = null) {
        val id = remoteId
        if (id == null) {
            viewModelScope.launch {
                _uiEvent.send(AdminOrderDetailUiEvent.ShowMessage("Không tìm thấy đơn hàng"))
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isActionLoading = true,
                    errorMessage = null,
                    pendingConfirmation = null
                )
            }
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
                _uiEvent.send(AdminOrderDetailUiEvent.ShowMessage("Cập nhật đơn hàng thành công"))
            }.onFailure { error ->
                val message = "Cập nhật đơn hàng không thành công. Vui lòng thử lại."
                _uiState.update {
                    it.copy(
                        isActionLoading = false,
                        errorMessage = message
                    )
                }
                _uiEvent.send(AdminOrderDetailUiEvent.ShowMessage(message))
            }
        }
    }
}
