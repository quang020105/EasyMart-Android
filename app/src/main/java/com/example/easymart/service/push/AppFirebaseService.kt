package com.example.easymart.service.push

import android.util.Log
import com.example.easymart.domain.repository.AuthRepository
import com.example.easymart.domain.repository.PaymentRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AppFirebaseService: FirebaseMessagingService() {
    @Inject
    lateinit var paymentRepository: PaymentRepository

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // token được tạo / refresh
        Log.d("FCM", "New token: $token")

        val userId = authRepository.getCurrentUser()?.id ?: return

        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                paymentRepository.registerDeviceToken(userId, token)
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("FCM", "Message: ${message.data}")
    }
}