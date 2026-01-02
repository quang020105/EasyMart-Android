package com.example.easymart.domain.payment

import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.PaymentResult
import kotlinx.coroutines.flow.Flow

interface PaymentProcess {
    fun process(order: Order): Flow<PaymentResult>
}