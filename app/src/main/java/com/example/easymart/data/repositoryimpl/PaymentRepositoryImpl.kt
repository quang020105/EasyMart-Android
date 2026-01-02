package com.example.easymart.data.repositoryimpl

import com.example.easymart.data.local.dao.OrderDao
import com.example.easymart.data.local.dao.WalletDao
import com.example.easymart.data.mapper.toEntity
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentResult
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.domain.payment.process.CODProcesser
import com.example.easymart.domain.payment.process.EWalletProcessor
import com.example.easymart.domain.payment.process.OnlineGatewayProcessor
import com.example.easymart.domain.payment.simulator.EWalletSimulator
import com.example.easymart.domain.payment.simulator.FakeGateway
import com.example.easymart.domain.repository.PaymentRepository
import com.example.easymart.presentation.navigation.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao,
    private val walletDao: WalletDao,
    private val codProcesser: CODProcesser,
    private val eWalletProcesser: EWalletProcessor,
    private val onlineGatewayProcesser: OnlineGatewayProcessor
): PaymentRepository {
    override fun processPayment(
        order: Order,
        method: PaymentMethod
    ): Flow<PaymentResult> = flow {
        //thêm đơn hàng vào db local
        orderDao.insertOrderWithItems(order = order.toEntity(), orderItems = order.items.map { it.toEntity() })
        val processor = when(method){
            PaymentMethod.COD -> codProcesser
            PaymentMethod.WALLET -> eWalletProcesser
            PaymentMethod.ONLINE_GATEWAY -> onlineGatewayProcesser
        }

        processor.process(order).collect{ result ->
            emit(result)
        }
    }

    override suspend fun getWalletBalance(userId: Int): Long {
        return walletDao.getBalance(userId) ?: 0L
    }

    override suspend fun deductWallet(userId: Int, amount: Long): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun saveOrderLocally(
        order: Order,
        status: PaymentStatus
    ) {
        TODO("Not yet implemented")
    }
}