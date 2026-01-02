package com.example.easymart.domain.usecase.payment

import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.repository.PaymentRepository
import javax.inject.Inject

class ProcessPaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
){
    operator fun invoke(order: Order, method: PaymentMethod) = paymentRepository.processPayment(order, method)
}