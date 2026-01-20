package com.example.easymart.presentation.ui.orderdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.usecase.order.GetOrderDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val getOrderDetailUseCase: GetOrderDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<OrderDetailUiState>(OrderDetailUiState.Loading)
    val uiState: StateFlow<OrderDetailUiState> = _uiState.asStateFlow()

    private val _events = Channel<OrderDetailUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun loadOrderDetail(orderId: Int){
        viewModelScope.launch {
            _uiState.value = OrderDetailUiState.Loading
            try {
                val order = getOrderDetailUseCase(orderId)
                if (order == null) {
                    _uiState.value = OrderDetailUiState.Error("Không tìm thấy đơn hàng.")
                } else {
                    _uiState.value = OrderDetailUiState.Success(order)
                }
            } catch (t: Throwable) {
                _uiState.value = OrderDetailUiState.Error(t.message ?: "Đã có lỗi xảy ra")
            }
        }
    }



}