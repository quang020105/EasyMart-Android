package com.example.easymart.presentation.sync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.network.NetworkStatus
import com.example.easymart.domain.usecase.address.SyncAddressesUseCase
import com.example.easymart.domain.usecase.auth.ObserveCurrentUserUseCase
import com.example.easymart.domain.usecase.cart.SyncCartUseCase
import com.example.easymart.domain.usecase.network.ObserveNetworkStatusUseCase
import com.example.easymart.domain.usecase.order.ObserveRemoteOrdersUseCase
import com.example.easymart.domain.usecase.order.SyncPendingOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppSyncViewModel @Inject constructor(
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase,
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val syncCartUseCase: SyncCartUseCase,
    private val syncAddressesUseCase: SyncAddressesUseCase,
    private val syncPendingOrdersUseCase: SyncPendingOrdersUseCase,
    private val observeRemoteOrdersUseCase: ObserveRemoteOrdersUseCase
) : ViewModel() {

    private var syncJob: Job? = null

    fun startSync() {
        if (syncJob?.isActive == true) return

        syncJob = viewModelScope.launch {
            observeCurrentUserUseCase()
                .map { user -> user?.id }
                .distinctUntilChanged()
                .collectLatest { userId ->
                    if (userId == null) return@collectLatest

                    observeNetworkStatusUseCase()
                        .distinctUntilChanged()
                        .collectLatest { networkStatus ->
                            if (networkStatus != NetworkStatus.Available) return@collectLatest

                            coroutineScope {
                                launch { syncLocalChanges(userId) }
                                observeRemoteOrdersUseCase(userId)
                                    .retryWhen { _, _ ->
                                        delay(5_000)
                                        true
                                    }
                                    .collect { }
                            }
                        }
                }
        }
    }

    private suspend fun syncLocalChanges(userId: String) {
        runCatching {
            syncCartUseCase(userId)
            syncAddressesUseCase(userId)
            syncPendingOrdersUseCase(userId)
        }
    }
}
