package com.example.easymart.domain.payment.simulator

import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.PaymentResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random

class EWalletSimulator (
    private val simulateNetworkDelayMs: Long = 800
){
    fun charge(order: Order): Flow<PaymentResult> = flow {
        emit(PaymentResult.Processing(orderId = order.id))
        delay(simulateNetworkDelayMs)
        val r = Random.Default.nextDouble()
        if (r < 0.85) {
            emit(PaymentResult.Success(orderId = order.id))
        } else {
            emit(PaymentResult.Failed(order.id, "E-wallet declined"))
        }
    }
}