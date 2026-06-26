package com.example.easymart.presentation.ui.orderdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.usecase.order.ObserveOrderDetailUseCase
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
    private val observeOrderDetailUseCase: ObserveOrderDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<OrderDetailUiState>(OrderDetailUiState.Loading)
    val uiState: StateFlow<OrderDetailUiState> = _uiState.asStateFlow()

    private val _events = Channel<OrderDetailUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var orderDetailJob: Job? = null

    fun loadOrderDetail(orderId: Int) {
        // hủy coroutine cũ nếu có để tránh tốn tài nguyên và tránh việc nhiều coroutine cùng chạy song song
        orderDetailJob?.cancel()
        orderDetailJob = viewModelScope.launch {
            observeOrderDetailUseCase(orderId)
                .catch { t ->
                    _uiState.value = OrderDetailUiState.Error(t.message ?: "Đã có lỗi xảy ra")
                }
                .collect { order ->
                    _uiState.value = if (order == null) {
                        OrderDetailUiState.Error("Không tìm thấy đơn hàng.")
                    } else {
                        OrderDetailUiState.Success(order)
                    }
                }
        }
    }
}
