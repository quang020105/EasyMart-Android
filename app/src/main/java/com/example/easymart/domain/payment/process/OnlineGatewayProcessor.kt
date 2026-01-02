package com.example.easymart.domain.payment.process

import com.example.easymart.data.local.dao.OrderDao
import com.example.easymart.data.local.dao.PaymentDao
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.PaymentResult
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.domain.payment.PaymentProcess
import com.example.easymart.domain.payment.simulator.FakeGateway
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OnlineGatewayProcessor @Inject constructor(
    private val gateway: FakeGateway,
    private val paymentDao: PaymentDao
) : PaymentProcess {
    override fun process(order: Order): Flow<PaymentResult> = flow {
        emit(PaymentResult.Processing(order.id))
        gateway.charge(order).collect { result ->
            when (result) {
                is PaymentResult.Success -> {
                    //cập nhật trạng thái thanh toán
                    paymentDao.updatePaymentStatus(order.id, PaymentStatus.SUCCESS)
                    emit(result)
                }

                is PaymentResult.Failed -> {
                    //cập nhật trạng thái thanh toán
                    paymentDao.updatePaymentStatus(order.id, PaymentStatus.FAILED)
                    emit(result)
                }

                else -> {
                    emit(result)
                }
            }

        }
    }
}