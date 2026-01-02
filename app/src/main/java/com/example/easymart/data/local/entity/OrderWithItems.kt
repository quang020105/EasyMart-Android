package com.example.easymart.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

//nối các bảng
data class OrderWithItems (
    @Embedded val order: OrderEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "orderId"
    )
    val items: List<OrderItemEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "orderId"
    )
    val payment: PaymentEntity?
)