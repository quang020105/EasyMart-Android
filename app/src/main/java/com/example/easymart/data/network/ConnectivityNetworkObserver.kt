package com.example.easymart.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.easymart.domain.network.NetworkObserver
import com.example.easymart.domain.network.NetworkStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

// theo dõi thay đổi trạng thái mạng
class ConnectivityNetworkObserver @Inject constructor(
    @param:ApplicationContext private val context: Context
) : NetworkObserver {

    override fun observe(): Flow<NetworkStatus> = callbackFlow {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(NetworkStatus.Available).isSuccess
            }

            override fun onLost(network: Network) {
                trySend(NetworkStatus.Unavailable).isSuccess
            }

            override fun onUnavailable() {
                trySend(NetworkStatus.Unavailable).isSuccess
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        val current = connectivityManager.activeNetwork
        val caps = connectivityManager.getNetworkCapabilities(current)
        val isOnline = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        trySend(if (isOnline) NetworkStatus.Available else NetworkStatus.Unavailable).isSuccess

        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }
}

