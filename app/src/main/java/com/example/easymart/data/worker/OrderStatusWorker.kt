package com.example.easymart.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.algolia.client.model.composition.Params
import com.example.easymart.data.local.dao.OrderDao
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class OrderStatusWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val orderDao: OrderDao
): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val orderId = inputData.getInt("orderId", -1)
        if(orderId == -1){
            return Result.failure()
        }
        val targetStatus = inputData.getString("targetStatus")?.let {
            OrderStatus.valueOf(it)
        } ?: return Result.failure()

        val order = orderDao.getOrderWithItems(orderId).order

        //nếu đơn hàng đã hủy thì không cập nhật trạng thái nữa
        if(order.orderStatus == OrderStatus.CANCELLED) {
            return Result.success()
        }
        //cập nhật trạng thái đơn hàng
        orderDao.updateOrderStatus(orderId = orderId, status = targetStatus)
        Log.d("OrderStatusWorker", "doWork: ${order.orderStatus}")
        return Result.success()
    }
}