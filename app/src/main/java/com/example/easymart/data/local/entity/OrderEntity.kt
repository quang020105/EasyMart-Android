package com.example.easymart.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentStatus

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val orderNumber: String,
    val totalAmount: Long,
    val orderStatus: OrderStatus = OrderStatus.CREATED,
    val paymentStatus: PaymentStatus = PaymentStatus.UNPAID,
    val shippingAddress: String,
    val createdAt: Long
)
