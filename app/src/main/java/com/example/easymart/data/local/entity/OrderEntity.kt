package com.example.easymart.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.domain.model.SyncStatus

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val orderNumber: String,
    val totalAmount: Long,
    val orderStatus: OrderStatus = OrderStatus.CREATED,
    val paymentStatus: PaymentStatus = PaymentStatus.UNPAID,
    val paymentMethod: PaymentMethod = PaymentMethod.COD,
    @Embedded(prefix = "shipping_") // nhúng địa chỉ giao hàng
    val shippingAddress: AddressEmbedded,
    val serverOrderId: Int? = null,    // server trả về
    val remoteId: String? = null,
    val isSynced: Boolean = false,
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val createdAt: Long,
    val updatedAt: Long = createdAt
)
