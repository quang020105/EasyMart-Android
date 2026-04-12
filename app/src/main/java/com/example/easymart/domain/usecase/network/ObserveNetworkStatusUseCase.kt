package com.example.easymart.domain.usecase.network

import com.example.easymart.domain.network.NetworkObserver
import com.example.easymart.domain.network.NetworkStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveNetworkStatusUseCase @Inject constructor(
    private val networkObserver: NetworkObserver
) {
    operator fun invoke(): Flow<NetworkStatus> = networkObserver.observe()
}

