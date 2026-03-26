package com.example.easymart.domain.repository

import com.example.easymart.data.repositoryimpl.PaymentEvent
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentResult
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.presentation.navigation.Screen
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {
    fun processPayment(order: Order, method: PaymentMethod): Flow<PaymentResult>
    suspend fun getWalletBalance(userId: String): Long
    suspend fun deductWallet(userId: String, amount: Long): Boolean
    suspend fun saveOrderLocally(order: Order, status: PaymentStatus)
    //đăng kí token
    suspend fun registerDeviceToken(userId: String, token: String)
    // lấy trạng thái thanh toán trả về từ server
    suspend fun handleRemotePaymentUpdate(
        serverOrderId: Int,
        status: String,
        providerRef: String?
    )
    fun observePaymentEvents(): Flow<PaymentEvent>
    suspend fun getPayOsOrderStatus(orderCode: Long): PaymentStatus
    suspend fun pollPayOsUntilDone(orderCode: Long, maxRetries: Int, delayMs: Long): PaymentStatus
    suspend fun updateLocalOrderPaymentStatus(localOrderId: Int, status: PaymentStatus)
}