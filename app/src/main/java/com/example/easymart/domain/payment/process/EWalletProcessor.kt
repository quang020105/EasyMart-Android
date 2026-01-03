package com.example.easymart.domain.payment.process

import com.example.easymart.data.local.dao.OrderDao
import com.example.easymart.data.local.dao.PaymentDao
import com.example.easymart.data.local.dao.WalletDao
import com.example.easymart.data.local.entity.PaymentEntity
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentResult
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.domain.payment.PaymentProcess
import com.example.easymart.domain.payment.simulator.EWalletSimulator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EWalletProcessor @Inject constructor(
    private val walletDao: WalletDao,
    private val paymentDao: PaymentDao,
    private val orderDao: OrderDao,
    private val eWalletSimulator: EWalletSimulator
) : PaymentProcess {
    override fun process(order: Order): Flow<PaymentResult> = flow {
        emit(PaymentResult.Processing(order.id))
        //kiểm tra số dư ví
        val balance = walletDao.getBalance(order.userId) ?: 0L
        if (balance < order.totalAmount) {
            val failedPaymentE = PaymentEntity(
                orderId = order.id,
                method = PaymentMethod.WALLET,
                amount = order.totalAmount,
                status = PaymentStatus.FAILED,
                createdAt = System.currentTimeMillis()
            )
            paymentDao.insertPayment(failedPaymentE)
            emit(PaymentResult.Failed(orderID = order.id, reason = "Số dư không đủ"))
            return@flow
        }

        //nếu đủ tiền thì tạo bản ghi thanh toán đang xử lý
        val processingPaymentE = PaymentEntity(
            orderId = order.id,
            method = PaymentMethod.WALLET,
            amount = order.totalAmount,
            status = PaymentStatus.PROCESSING,
            createdAt = System.currentTimeMillis()
        )
        //lưu lại paymentId để phục vụ cập nhật ở phần sau
        //val paymentId =
        paymentDao.insertPayment(processingPaymentE)

        //trừ tiền luôn
        walletDao.updateBalance(order.userId, balance - order.totalAmount)
        //mô phỏng gọi thanh toán
        eWalletSimulator.charge(order).collect { result ->
            when (result) {
                is PaymentResult.Success -> {
                    //cập nhật trạng thái thanh toán
                    paymentDao.updatePaymentStatus(order.id, PaymentStatus.SUCCESS)
                    //cập nhật đơn hàng
                    orderDao.updateOrderAndPaymentStatus(
                        order.id,
                        OrderStatus.CONFIRMED,
                        PaymentStatus.SUCCESS
                    )
                    emit(result)
                }

                is PaymentResult.Failed -> {
                    //nếu thất bại thì hoàn tiền
                    walletDao.updateBalance(order.userId, balance)
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