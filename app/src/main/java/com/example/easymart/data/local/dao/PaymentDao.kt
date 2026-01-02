package com.example.easymart.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.easymart.domain.model.PaymentStatus

@Dao
interface PaymentDao {
    @Query("UPDATE payments SET status = :status WHERE orderId = :orderId")
    suspend fun updatePaymentStatus(orderId: Int, status: PaymentStatus)
}