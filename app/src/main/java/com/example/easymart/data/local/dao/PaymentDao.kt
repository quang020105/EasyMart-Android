package com.example.easymart.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.easymart.data.local.entity.PaymentEntity
import com.example.easymart.domain.model.PaymentStatus

@Dao
interface PaymentDao {
    @Insert
    suspend fun insertPayment(payment: PaymentEntity): Long

    @Query("UPDATE payments SET status = :status WHERE orderId = :orderId")
    suspend fun updatePaymentStatus(orderId: Int, status: PaymentStatus)

    //lấy thông tin thanh toán cuối cùng của đơn hàng vì mỗi đơn có thể có nhiều bản ghi thanh toán
    @Query("SELECT * FROM payments WHERE orderId = :orderId ORDER BY createdAt desc LIMIT 1")
    suspend fun getLastestPaymentByOrderId(orderId: Int): PaymentEntity?
}