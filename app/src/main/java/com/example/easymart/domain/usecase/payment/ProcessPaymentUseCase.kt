package com.example.easymart.domain.usecase.payment

import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.SyncStatus
import com.example.easymart.domain.repository.OrderRepository
import com.example.easymart.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject

class ProcessPaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository
){
    operator fun invoke(order: Order, method: PaymentMethod) = flow {
        val remoteId = UUID.randomUUID().toString()
        val localOrderId = orderRepository.saveOrderLocally(
            order.copy(
                remoteId = remoteId,
                isSynced = false,
                syncStatus = SyncStatus.PENDING
            )
        )
        val localOrder = order.copy(
            id = localOrderId,
            remoteId = remoteId,
            orderNumber = "ORD-$localOrderId",
            isSynced = false,
            syncStatus = SyncStatus.PENDING
        )
        emitAll(paymentRepository.processPayment(localOrder, method))
    }
}
