package com.example.easymart.presentation.ui.admin.orders.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.domain.usecase.auth.GetCurrentUserUseCase
import com.example.easymart.domain.usecase.order.ApproveOrderCancellationUseCase
import com.example.easymart.domain.usecase.order.ConfirmManualRefundUseCase
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
    private val updateAdminOrderStatusUseCase: UpdateAdminOrderStatusUseCase,
    private val approveOrderCancellationUseCase: ApproveOrderCancellationUseCase,
    private val confirmManualRefundUseCase: ConfirmManualRefundUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
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

    fun cancelOrder() = requestConfirmation(
        AdminOrderConfirmation(
            title = "Duyệt hủy đơn",
            message = "Xác nhận hủy đơn? Tồn kho sẽ được hoàn lại. Đơn đã thanh toán sẽ chuyển sang chờ hoàn tiền.",
            confirmText = "Duyệt hủy",
            isDanger = true,
            actionType = AdminOrderActionType.APPROVE_CANCELLATION,
            orderStatus = OrderStatus.CANCELLED
        )
    )

    fun confirmRefund() = requestConfirmation(
        AdminOrderConfirmation(
            title = "Xác nhận hoàn tiền",
            message = "Xác nhận admin đã hoàn tiền thủ công cho khách hàng?",
            confirmText = "Đã hoàn tiền",
            isDanger = false,
            actionType = AdminOrderActionType.CONFIRM_MANUAL_REFUND,
            orderStatus = OrderStatus.CANCELLED
        )
    )

    fun moveToProcessing() = requestStatusChange(
        orderStatus = OrderStatus.PACKING,
        title = "Chuyển sang chờ lấy hàng",
        message = "Chuyển đơn hàng sang trạng thái chờ lấy hàng?",
        confirmText = "Chuyển trạng thái"
    )

    fun moveToShipping() = requestStatusChange(
        orderStatus = OrderStatus.SHIPPING,
        title = "Chuyển sang đang giao",
        message = "Chuyển đơn hàng sang trạng thái đang giao?",
        confirmText = "Chuyển trạng thái"
    )

    fun confirmDelivered() {
        val paymentStatus = when (_uiState.value.order?.paymentStatus) {
            PaymentStatus.PAID -> null
            else -> PaymentStatus.PAID
        }
        requestStatusChange(
            orderStatus = OrderStatus.DELIVERED,
            paymentStatus = paymentStatus,
            title = "Xác nhận đã giao",
            message = "Xác nhận đơn hàng đã được giao thành công?",
            confirmText = "Xác nhận"
        )
    }

    fun dismissConfirmation() {
        _uiState.update { it.copy(pendingConfirmation = null) }
    }

    fun confirmPendingAction() {
        val confirmation = _uiState.value.pendingConfirmation ?: return
        when (confirmation.actionType) {
            AdminOrderActionType.UPDATE_STATUS -> updateStatus(
                orderStatus = confirmation.orderStatus,
                paymentStatus = confirmation.paymentStatus
            )
            AdminOrderActionType.APPROVE_CANCELLATION -> approveCancellation()
            AdminOrderActionType.CONFIRM_MANUAL_REFUND -> confirmManualRefund()
        }
    }

    private fun requestStatusChange(
        orderStatus: OrderStatus,
        title: String,
        message: String,
        confirmText: String,
        paymentStatus: PaymentStatus? = null,
        isDanger: Boolean = false
    ) = requestConfirmation(
        AdminOrderConfirmation(
            title = title,
            message = message,
            confirmText = confirmText,
            isDanger = isDanger,
            actionType = AdminOrderActionType.UPDATE_STATUS,
            orderStatus = orderStatus,
            paymentStatus = paymentStatus
        )
    )

    private fun requestConfirmation(confirmation: AdminOrderConfirmation) {
        if (_uiState.value.isActionLoading) return
        _uiState.update { it.copy(pendingConfirmation = confirmation) }
    }

    private fun updateStatus(orderStatus: OrderStatus, paymentStatus: PaymentStatus? = null) {
        runAdminAction("Cập nhật đơn hàng thành công") { id, _ ->
            updateAdminOrderStatusUseCase(id, orderStatus, paymentStatus)
        }
    }

    private fun approveCancellation() = runAdminAction(
        "Đã duyệt hủy đơn và cập nhật tồn kho"
    ) { id, adminId ->
        approveOrderCancellationUseCase(id, adminId)
    }

    private fun confirmManualRefund() = runAdminAction(
        "Đã xác nhận hoàn tiền"
    ) { id, adminId ->
        confirmManualRefundUseCase(id, adminId)
    }

    private fun runAdminAction(
        successMessage: String,
        action: suspend (remoteId: String, adminId: String) -> Unit
    ) {
        val id = remoteId
        val adminId = getCurrentUserUseCase()?.id
        if (id == null || adminId == null) {
            viewModelScope.launch {
                _uiEvent.send(AdminOrderDetailUiEvent.ShowMessage("Không xác định được tài khoản hoặc đơn hàng"))
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
                action(id, adminId)
                getAdminOrderDetailUseCase(id)
            }.onSuccess { order ->
                _uiState.update {
                    it.copy(
                        isActionLoading = false,
                        order = order?.toAdminOrderDetailUiModel(),
                        errorMessage = null
                    )
                }
                _uiEvent.send(AdminOrderDetailUiEvent.ShowMessage(successMessage))
            }.onFailure {
                val message = "Không thể hoàn tất thao tác. Vui lòng thử lại."
                _uiState.update { it.copy(isActionLoading = false, errorMessage = message) }
                _uiEvent.send(AdminOrderDetailUiEvent.ShowMessage(message))
            }
        }
    }
}
