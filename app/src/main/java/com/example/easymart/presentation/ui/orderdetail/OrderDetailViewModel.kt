package com.example.easymart.presentation.ui.orderdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.order.OrderCancellationPolicy
import com.example.easymart.domain.usecase.auth.GetCurrentUserUseCase
import com.example.easymart.domain.usecase.order.CancelCreatedOrderUseCase
import com.example.easymart.domain.usecase.order.ObserveOrderDetailUseCase
import com.example.easymart.domain.usecase.order.RequestOrderCancellationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val observeOrderDetailUseCase: ObserveOrderDetailUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val requestOrderCancellationUseCase: RequestOrderCancellationUseCase,
    private val cancelCreatedOrderUseCase: CancelCreatedOrderUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<OrderDetailUiState>(OrderDetailUiState.Loading)
    val uiState: StateFlow<OrderDetailUiState> = _uiState.asStateFlow()

    private val _events = Channel<OrderDetailUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val _cancellationState = MutableStateFlow(OrderCancellationUiState())
    val cancellationState: StateFlow<OrderCancellationUiState> = _cancellationState.asStateFlow()

    private var orderDetailJob: Job? = null

    fun loadOrderDetail(orderId: Int) {
        orderDetailJob?.cancel()
        orderDetailJob = viewModelScope.launch {
            observeOrderDetailUseCase(orderId)
                .catch { error ->
                    _uiState.value = OrderDetailUiState.Error(error.message ?: "Không thể tải đơn hàng")
                }
                .collect { order ->
                    _uiState.value = if (order == null) {
                        OrderDetailUiState.Error("Không tìm thấy đơn hàng")
                    } else {
                        OrderDetailUiState.Success(order)
                    }
                }
        }
    }

    fun requestCancellation() {
        val order = (uiState.value as? OrderDetailUiState.Success)?.order ?: return
        val mode = when {
            OrderCancellationPolicy.canCustomerCancelImmediately(order) -> {
                CustomerCancellationMode.DIRECT
            }
            OrderCancellationPolicy.canCustomerRequestCancellation(order) -> {
                CustomerCancellationMode.REQUEST
            }
            else -> null
        }
        if (mode == null) {
            viewModelScope.launch {
                _events.send(OrderDetailUiEvent.ShowToast("Đơn hàng hiện không thể hủy"))
            }
            return
        }
        _cancellationState.value = OrderCancellationUiState(
            pendingOrderId = order.id,
            mode = mode
        )
    }

    fun updateCancellationReason(reason: String) {
        _cancellationState.value = _cancellationState.value.copy(reason = reason)
    }

    fun dismissCancellation() {
        if (!_cancellationState.value.isSubmitting) {
            _cancellationState.value = OrderCancellationUiState()
        }
    }

    fun confirmCancellation() {
        val cancellation = _cancellationState.value
        val orderId = cancellation.pendingOrderId ?: return
        val userId = getCurrentUserUseCase()?.id
        if (userId == null) {
            viewModelScope.launch {
                _events.send(OrderDetailUiEvent.ShowToast("Vui lòng đăng nhập để hủy đơn hàng"))
            }
            return
        }

        viewModelScope.launch {
            val reason = cancellation.reason.trim().ifBlank { "Thay đổi nhu cầu mua hàng" }
            _cancellationState.value = cancellation.copy(isSubmitting = true)
            runCatching {
                when (cancellation.mode) {
                    CustomerCancellationMode.DIRECT -> {
                        cancelCreatedOrderUseCase(orderId, userId, reason)
                    }
                    CustomerCancellationMode.REQUEST -> {
                        requestOrderCancellationUseCase(orderId, userId, reason)
                    }
                }
            }.onSuccess {
                _cancellationState.value = OrderCancellationUiState()
                val message = if (cancellation.mode == CustomerCancellationMode.DIRECT) {
                    "Đã hủy đơn hàng"
                } else {
                    "Đã gửi yêu cầu hủy đơn"
                }
                _events.send(OrderDetailUiEvent.ShowToast(message))
            }.onFailure { error ->
                _cancellationState.value = cancellation.copy(isSubmitting = false)
                _events.send(
                    OrderDetailUiEvent.ShowToast(error.message ?: "Không thể xử lý yêu cầu hủy đơn")
                )
            }
        }
    }
}
