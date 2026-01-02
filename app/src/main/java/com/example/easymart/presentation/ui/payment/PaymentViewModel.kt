package com.example.easymart.presentation.ui.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentResult
import com.example.easymart.domain.repository.PaymentRepository
import com.example.easymart.domain.usecase.payment.GetWalletBalanceUseCase
import com.example.easymart.domain.usecase.payment.ProcessPaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val processPaymentUseCase: ProcessPaymentUseCase,
    private val getWalletBalanceUseCase: GetWalletBalanceUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState

    fun selectPaymentMethod(method: PaymentMethod) {
        _uiState.update { it.copy(selectedMethod = method) }
    }

    fun pay(order: Order) {
        viewModelScope.launch {

            processPaymentUseCase(order, _uiState.value.selectedMethod!!)
                //onStart để cập nhật trạng thái xử lý trước khi bắt đầu thu thập kết quả
                .onStart {
                    _uiState.update { it.copy(isProcessing = true) }
                }
                .collect { result ->
                    _uiState.update {
                        it.copy(
                            isProcessing = result is PaymentResult.Processing,
                            lastResult = result
                        )
                    }
                }
        }
    }

    fun clearResult(){
        _uiState.update { it.copy(lastResult = null) }
    }
}