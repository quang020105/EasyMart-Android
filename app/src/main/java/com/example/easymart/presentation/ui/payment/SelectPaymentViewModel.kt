package com.example.easymart.presentation.ui.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.usecase.payment.GetWalletBalanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectPaymentViewModel @Inject constructor(
    private val getWalletBalanceUseCase: GetWalletBalanceUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState

    fun selectPaymentMethod(method: PaymentMethod) {
        _uiState.update { it.copy(selectedMethod = method) }
    }

//    fun pay(order: Order) {
//        viewModelScope.launch {
//
//            processPaymentUseCase(order, _uiState.value.selectedMethod!!)
//                //onStart để cập nhật trạng thái xử lý trước khi bắt đầu thu thập kết quả
//                .onStart {
//                    _uiState.update { it.copy(isProcessing = true) }
//                }
//                .collect { result ->
//                    _uiState.update {
//                        it.copy(
//                            isProcessing = result is PaymentResult.Processing,
//                            lastResult = result
//                        )
//                    }
//                }
//        }
//    }

//    fun clearResult(){
//        _uiState.update { it.copy(lastResult = null) }
//    }

    fun loadWalletBalance(userId: String) {
        viewModelScope.launch {
            val balance = getWalletBalanceUseCase(userId = userId)
            _uiState.update { it.copy(walletBalance = balance) }
        }
    }
}