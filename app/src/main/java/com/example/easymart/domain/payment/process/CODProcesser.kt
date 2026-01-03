package com.example.easymart.domain.payment.process

import com.example.easymart.data.local.dao.OrderDao
import com.example.easymart.data.local.dao.PaymentDao
import com.example.easymart.data.local.entity.PaymentEntity
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentResult
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.domain.payment.PaymentProcess
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CODProcesser @Inject constructor(
    private val orderDao: OrderDao,
    private val paymentDao: PaymentDao
): PaymentProcess {
    override fun process(order: Order): Flow<PaymentResult> = flow {
        emit(PaymentResult.Processing(orderId = order.id))
        //giả định chờ
        delay(500)
        //thêm bản ghi thanh toán vào room
        val paymentE = PaymentEntity(
            orderId = order.id,
            method = PaymentMethod.COD,
            status = PaymentStatus.UNPAID,
            amount = order.totalAmount,
            createdAt = System.currentTimeMillis()
        )
        paymentDao.insertPayment(paymentE)

        orderDao.updateOrderAndPaymentStatus(
            orderId = order.id,
            status = OrderStatus.CONFIRMED,
            paymentStatus = PaymentStatus.UNPAID
        )
        emit(PaymentResult.Success(orderId = order.id, status = PaymentStatus.UNPAID))
    }
}