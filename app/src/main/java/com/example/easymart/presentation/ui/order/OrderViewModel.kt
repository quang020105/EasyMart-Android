package com.example.easymart.presentation.ui.order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.usecase.order.GetObserveAllOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
     private val getObserveAllOrdersUseCase: GetObserveAllOrdersUseCase
): ViewModel() {
     private val _uiState: MutableStateFlow<OrderListUiState> = MutableStateFlow(OrderListUiState.Loading)
     val uiState: StateFlow<OrderListUiState> = _uiState.asStateFlow()

     private val _uiEvent = Channel<OrderUiEvent>(Channel.BUFFERED)
     val uiEvent = _uiEvent.receiveAsFlow()

     init {
          observeOrders()
     }

     // lắng nghe danh sách đơn hàng
     private fun observeOrders(){
          viewModelScope.launch {
               getObserveAllOrdersUseCase("1") //chưa xủ lý userId
                    .onStart { _uiState.value = OrderListUiState.Loading }
                    .catch { throwable ->
                         _uiState.value =
                              OrderListUiState.Error(throwable.message ?: "Đã có lỗi xảy ra")
                    }
                    .collect { orders->
                         if(orders.isEmpty()){
                              _uiState.value = OrderListUiState.Empty
                         } else {
                                _uiState.value = OrderListUiState.Success(orders)
                         }
                         Log.d("OrderViewModel", "observeOrders: $orders")
                    }
          }
     }

     fun onViewOrderDetail(orderId: Int){
          viewModelScope.launch {
               _uiEvent.send(OrderUiEvent.NavigateToOrderDetail(orderId))
          }
     }

     fun onPrimaryActionForOrder(order: Order){
          viewModelScope.launch {
               when(order.status){
                    OrderStatus.DELIVERED, OrderStatus.CANCELLED  -> _uiEvent.send(OrderUiEvent.NavigateToBuyAgain(order.id))
                    OrderStatus.SHIPPING-> _uiEvent.send(OrderUiEvent.NavigateToTrack(order.id))
                    OrderStatus.CREATED,  OrderStatus.CONFIRMED  -> {
                            //todo: hủy đơn
                    }
                    else -> {}
               }
          }
     }
}