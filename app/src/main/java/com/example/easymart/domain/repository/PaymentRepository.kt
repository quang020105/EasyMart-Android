package com.example.easymart.domain.repository

import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentResult
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.presentation.navigation.Screen
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {
    fun processPayment(order: Order, method: PaymentMethod): Flow<PaymentResult>
    suspend fun getWalletBalance(userId: Int): Long
    suspend fun deductWallet(userId: Int, amount: Long): Boolean
    suspend fun saveOrderLocally(order: Order, status: PaymentStatus)
}