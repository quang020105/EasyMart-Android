package com.example.easymart.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.easymart.data.local.entity.OrderEntity
import com.example.easymart.data.local.entity.OrderItemEntity
import com.example.easymart.data.local.entity.PaymentEntity

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