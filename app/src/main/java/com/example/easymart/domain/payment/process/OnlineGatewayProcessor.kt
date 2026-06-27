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
import com.example.easymart.domain.payment.simulator.FakeGateway
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OnlineGatewayProcessor @Inject constructor(
    private val gateway: FakeGateway,
    private val paymentDao: PaymentDao,
    private val orderDao: OrderDao
) : PaymentProcess {
    override fun process(order: Order): Flow<PaymentResult> = flow {
        emit(PaymentResult.Processing(order.id))

        val processingPaymentE = PaymentEntity(
            orderId = order.id,
            method = PaymentMethod.WALLET,
            amount = order.totalAmount,
            status = PaymentStatus.PROCESSING,
            createdAt = System.currentTimeMillis()
        )
        paymentDao.insertPayment(processingPaymentE)

        gateway.charge(order).collect { result ->
            when (result) {
                is PaymentResult.Success -> {
                    //cập nhật trạng thái thanh toán
                    paymentDao.updatePaymentStatus(order.id, PaymentStatus.PAID)
                    //cập nhật đơn hàng
                    orderDao.updateOrderAndPaymentStatus(
                        order.id,
                        OrderStatus.CONFIRMED,
                        PaymentStatus.PAID
                    )
                    emit(result)
                }

                is PaymentResult.Failed -> {
                    //cập nhật trạng thái thanh toán
                    paymentDao.updatePaymentStatus(order.id, PaymentStatus.FAILED)
                    //cập nhật đơn hàng
                    orderDao.updateOrderAndPaymentStatus(
                        order.id,
                        OrderStatus.CANCELLED,
                        PaymentStatus.FAILED
                    )
                    emit(result)
                }

                else -> {
                    emit(result)
                }
            }
        }
    }
}