package com.example.easymart.presentation.sync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.network.NetworkStatus
import com.example.easymart.domain.usecase.auth.GetCurrentUserUseCase
import com.example.easymart.domain.usecase.cart.SyncCartUseCase
import com.example.easymart.domain.usecase.network.ObserveNetworkStatusUseCase
import com.example.easymart.domain.usecase.address.SyncAddressesUseCase
import com.example.easymart.domain.usecase.order.SyncPendingOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppSyncViewModel @Inject constructor(
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val syncCartUseCase: SyncCartUseCase,
    private val syncAddressesUseCase: SyncAddressesUseCase,
    private val syncPendingOrdersUseCase: SyncPendingOrdersUseCase
) : ViewModel() {

    fun startSync() {
        viewModelScope.launch {
            observeNetworkStatusUseCase()
                .distinctUntilChanged()    //chỉ emit khi giá trị thay đổi
                .filter { it == NetworkStatus.Available }
                .collectLatest {
                    val userId = getCurrentUserUseCase()?.id ?: return@collectLatest
                    syncCartUseCase(userId)
                    syncAddressesUseCase(userId)
                    syncPendingOrdersUseCase(userId)
                }
        }
    }
}
