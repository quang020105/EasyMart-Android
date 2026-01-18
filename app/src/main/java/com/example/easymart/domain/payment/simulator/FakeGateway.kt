package com.example.easymart.domain.payment.simulator

import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.PaymentResult
import com.example.easymart.domain.model.PaymentStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class FakeGateway (
    private val delayMs: Long = 1000L
){
    fun charge(order: Order): Flow<PaymentResult> = flow {
        emit(PaymentResult.Processing(order.id))
        delay(delayMs)

        val r = Random.Default.nextDouble()
        when {
            r < 0.75 -> emit(PaymentResult.Success(order.id, PaymentStatus.SUCCESS))
            r < 0.9 -> emit(PaymentResult.Failed(order.id, "Thẻ không hợp lệ"))
            else -> emit(PaymentResult.Failed(order.id, "Hết thời gian chờ, hãy thử lại"))
        }
    }
}