package com.example.easymart.data.mapper

import com.example.easymart.data.local.entity.PaymentEntity
import com.example.easymart.domain.model.Payment
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentStatus

fun PaymentEntity.toDomain(): Payment {
    return Payment(
        id = id,
        orderId = orderId,
        method = PaymentMethod.valueOf(method.name),
        status = PaymentStatus.valueOf(status.name),
        amount = amount,
        createdAt = createdAt.toString()
    )
}

fun Payment.toEntity(): PaymentEntity {
    return PaymentEntity(
        id = id,
        orderId = orderId,
        method = PaymentMethod.valueOf(method.name),
        status = PaymentStatus.valueOf(status.name),
        amount = amount,
        createdAt = System.currentTimeMillis()
    )
}

