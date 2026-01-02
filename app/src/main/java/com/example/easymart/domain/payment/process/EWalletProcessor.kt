package com.example.easymart.domain.payment.process

import com.example.easymart.data.local.dao.OrderDao
import com.example.easymart.data.local.dao.PaymentDao
import com.example.easymart.data.local.dao.WalletDao
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.PaymentResult
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.domain.payment.PaymentProcess
import com.example.easymart.domain.payment.simulator.EWalletSimulator
import com.example.easymart.domain.payment.simulator.FakeGateway
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EWalletProcessor @Inject constructor(
    private val walletDao: WalletDao,
    private val eWalletSimulator: EWalletSimulator,
    private val paymentDao: PaymentDao
) : PaymentProcess {
    override fun process(order: Order): Flow<PaymentResult> = flow {
        emit(PaymentResult.Processing(order.id))
        //kiểm tra số dư ví
        val balance = walletDao.getBalance(order.userId) ?: 0L
        if (balance < order.totalAmount) {
            emit(PaymentResult.Failed(orderID = order.id, reason = "Số dư không đủ"))
            return@flow
        }

        //nếu đủ thì trừ tiền luôn
        walletDao.updateBalance(order.userId, balance - order.totalAmount)
        //mô phỏng gọi thanh toán
        eWalletSimulator.charge(order).collect { result ->
            when (result) {
                is PaymentResult.Success -> {
                    //cập nhật trạng thái thanh toán
                    paymentDao.updatePaymentStatus(order.id, PaymentStatus.SUCCESS)
                    emit(result)
                }

                is PaymentResult.Failed -> {
                    //nếu thất bại thì hoàn tiền
                    walletDao.updateBalance(order.userId, balance)
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