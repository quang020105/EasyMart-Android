package com.example.easymart.domain.usecase.order

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.easymart.data.worker.OrderStatusWorker
import com.example.easymart.domain.model.OrderStatus
import java.util.concurrent.TimeUnit
import javax.inject.Inject

//cần check lại thời gian chuyển trạng đơn hàng
class OrderAutoProcessUseCase @Inject constructor(
    private val context: Context,
) {
    //nếu đặt hàng thành công thì tự động chuyển trạng thái đơn hàng bằng workManager
    // (giả lập quy trình xử lý đơn hàng)
    fun start(orderId: Int){
        val workManager = WorkManager.getInstance(context)
        //chuỗi công việc chuyển trạng thái đơn hàng

        val confirmedDelay = (20..50).random().toLong()
        val processingDelay = confirmedDelay + (20..30).random()
        val shippingDelay = processingDelay + (60..150).random()
        val deliveredDelay = shippingDelay + (600..3000).random()

//        val confirmedDelay = (5..10).random().toLong()
//        val processingDelay = confirmedDelay + (5..10).random()
//        val shippingDelay = processingDelay + (5..10).random()
//        val deliveredDelay = shippingDelay + (5..10).random()

        val confirmed = buildWork(orderId, OrderStatus.CONFIRMED, confirmedDelay)
        val processing = buildWork(orderId, OrderStatus.PROCESSING, processingDelay)
        val shipping = buildWork(orderId, OrderStatus.SHIPPING, shippingDelay)
        val delivered = buildWork(orderId, OrderStatus.DELIVERED, deliveredDelay)

        //thực hiện chuỗi công việc (then chỉ đảm bảo thứ tự chứ không đảm bảo thời gian delay)
        workManager.beginWith(confirmed)
            .then(processing)
            .then(shipping)
            .then(delivered)
            .enqueue()
    }

    //khi hủy đơn hàng thì hủy luôn luồng chuyển trạng thái đơn hàng tự động
    fun cancel(orderId: Int){
        WorkManager.getInstance(context)
            .cancelAllWorkByTag(orderTag(orderId))
    }

    private fun buildWork(
        orderId: Int,
        targetStatus: OrderStatus,
        delaySeconds: Long
    ) = OneTimeWorkRequestBuilder<OrderStatusWorker>()
            .setInitialDelay(delaySeconds, TimeUnit.SECONDS)
            .addTag(orderTag(orderId))
            .setInputData(
                workDataOf(
                    "orderId" to orderId,
                    "targetStatus" to targetStatus.name
                )
            )
            .build()


    //tag để hủy luồng chuyển trạng thái đơn hàng
    private fun orderTag(orderId: Int) = "order_$orderId"
}