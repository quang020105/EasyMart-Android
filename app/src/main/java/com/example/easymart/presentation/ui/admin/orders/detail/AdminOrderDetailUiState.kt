package com.example.easymart.presentation.ui.admin.orders.detail

import androidx.compose.runtime.Immutable
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentStatus

@Immutable
data class AdminOrderDetailUiState(
    val isLoading: Boolean = false,
    val isActionLoading: Boolean = false,
    val order: AdminOrderDetailUiModel? = null,
    val errorMessage: String? = null,
    val pendingConfirmation: AdminOrderConfirmation? = null
)


// dùng cho hộp thoại xác nhận
@Immutable
data class AdminOrderConfirmation(
    val title: String,
    val message: String,
    val confirmText: String,
    val isDanger: Boolean,
    val actionType: AdminOrderActionType,
    val orderStatus: OrderStatus,
    val paymentStatus: PaymentStatus? = null
)

enum class AdminOrderActionType {
    UPDATE_STATUS,
    APPROVE_CANCELLATION,
    CONFIRM_MANUAL_REFUND
}
