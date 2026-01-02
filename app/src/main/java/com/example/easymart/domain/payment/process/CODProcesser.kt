package com.example.easymart.domain.payment.process

import com.example.easymart.data.local.dao.OrderDao
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.PaymentResult
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.domain.payment.PaymentProcess
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CODProcesser @Inject constructor(
    private val orderDao: OrderDao
): PaymentProcess {
    override fun process(order: Order): Flow<PaymentResult> = flow {
        emit(PaymentResult.Processing(orderId = order.id))
        //cập nhật trạng thái thanh toán vào room
        //todo

        //giả định chờ
        delay(500)
        emit(PaymentResult.Success(orderId = order.id, status = PaymentStatus.PENDING))
    }
}