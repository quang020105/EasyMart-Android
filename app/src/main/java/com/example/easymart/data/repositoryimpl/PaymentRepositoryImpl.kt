package com.example.easymart.data.repositoryimpl

import android.util.Log
import com.example.easymart.data.local.dao.OrderDao
import com.example.easymart.data.local.dao.WalletDao
import com.example.easymart.data.remote.api.PaymentApi
import com.example.easymart.data.remote.dto.PayOsItemDto
import com.example.easymart.data.remote.dto.payment.PayOsCreatePaymentRequest
import com.example.easymart.data.remote.dto.payment.RegisterDeviceTokenRequest
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentResult
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.domain.payment.process.CODProcessor
import com.example.easymart.domain.payment.process.EWalletProcessor
import com.example.easymart.domain.payment.process.OnlineGatewayProcessor
import com.example.easymart.domain.repository.PaymentRepository
import com.example.easymart.utils.toVNDLong
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao,
    private val walletDao: WalletDao,
    private val paymentApi: PaymentApi,
    private val codProcessor: CODProcessor,
    private val eWalletProcesser: EWalletProcessor,
    private val onlineGatewayProcesser: OnlineGatewayProcessor,
) : PaymentRepository {
    private val paymentEvents = MutableSharedFlow<PaymentEvent>(extraBufferCapacity = 1)

    override fun observePaymentEvents(): Flow<PaymentEvent> = paymentEvents

    override fun processPayment(
        order: Order,
        method: PaymentMethod
    ): Flow<PaymentResult> = flow {
        when (method) {

            PaymentMethod.COD,
            PaymentMethod.WALLET -> {
                val processor = if (method == PaymentMethod.COD) codProcessor else eWalletProcesser
                processor.process(order).collect { emit(it) }
            }

            PaymentMethod.ONLINE_GATEWAY -> {
                emit(PaymentResult.Pending(order.id))

                val payOsResp = paymentApi.createPayOsPayment(
                    PayOsCreatePaymentRequest(
                        amount = order.totalAmount.toVNDLong(),
                        description = "Thanh toán cho EasyMart",
                        localOrderId = order.id.toLong(),
                        items = order.items.map { orderItem ->
                            PayOsItemDto(
                                name = orderItem.product.name,
                                quantity = orderItem.quantity,
                                price = orderItem.product.price.toVNDLong()
                            )
                        }
                    )
                )

                Log.d("PaymentRepositoryImpl", "PayOS checkoutUrl=${payOsResp.checkoutUrl} orderCode=${payOsResp.orderCode}")

                emit(
                    PaymentResult.Redirect(
                        localOrderId = order.id,
                        deeplink = payOsResp.checkoutUrl,
                        qrImageUrl = payOsResp.qrCode,
                        orderCode = payOsResp.orderCode
                    )
                )
            }
        }
    }

    override suspend fun getWalletBalance(userId: String): Long {
        return walletDao.getBalance(userId) ?: 0L
    }

    override suspend fun deductWallet(userId: String, amount: Long): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun registerDeviceToken(userId: String, token: String) {
        runCatching {
            paymentApi.registerDeviceToken(RegisterDeviceTokenRequest(userId, token))
        }
    }

    override suspend fun handleRemotePaymentUpdate(
        serverOrderId: Int,
        status: String,
        providerRef: String?
    ) {
        orderDao.updatePaymentStatusByServerId(serverOrderId, status)

        val order = orderDao.getByServerOrderId(serverOrderId)
        val localId = order?.id ?: return

        paymentEvents.emit(
            PaymentEvent(
                serverOrderId = serverOrderId,
                localOrderId = localId,
                status = status,
                providerRef = providerRef
            )
        )
    }

    override suspend fun getPayOsOrderStatus(orderCode: Long): PaymentStatus {
        // Poll endpoint chuẩn (backend đã map theo webhook)
        val status = paymentApi.getPayOsPaymentStatus(orderCode).status
        return when (status.uppercase()) {
            "PAID", "SUCCESS" -> PaymentStatus.SUCCESS
            "CANCELLED", "CANCELED" -> PaymentStatus.FAILED
            "FAILED" -> PaymentStatus.FAILED
            else -> PaymentStatus.PENDING
        }
    }

    override suspend fun pollPayOsUntilDone(
        orderCode: Long,
        maxRetries: Int,
        delayMs: Long
    ): PaymentStatus {
        var attempt = 0
        while (attempt < maxRetries) {
            val status = getPayOsOrderStatus(orderCode)
            if (status == PaymentStatus.SUCCESS || status == PaymentStatus.FAILED) {
                return status
            }
            attempt++
            delay(delayMs)
            Log.d("PaymentRepositoryImpl", "Polling PayOS orderCode=$orderCode attempt=$attempt status=$status")
        }
        return PaymentStatus.PENDING
    }

    override suspend fun updateLocalOrderPaymentStatus(localOrderId: Int, status: PaymentStatus) {
        val orderStatus = when (status) {
            PaymentStatus.SUCCESS -> OrderStatus.CONFIRMED
            PaymentStatus.FAILED -> OrderStatus.CANCELLED
            PaymentStatus.UNPAID, PaymentStatus.PROCESSING, PaymentStatus.PENDING ->
                OrderStatus.CREATED
        }
        orderDao.updateOrderAndPaymentStatus(localOrderId, orderStatus, status)
    }
}

data class PaymentEvent(
    val serverOrderId: Int,
    val localOrderId: Int,
    val status: String,
    val providerRef: String?
)
