package com.example.easymart.domain.model

data class Payment(
    val id: Int,
    val orderId: Int,
    val method: PaymentMethod,
    val status: PaymentStatus,
    val amount: Long,
    val createdAt: String
)

enum class PaymentStatus {
    UNPAID, //chưa thanh toán
    PROCESSING, // đang xử lý
    SUCCESS, //thanh toán thành công
    FAILED, //thanh toán thất bại
    PENDING //đang chờ xác nhận từ cổng thanh toán
}

enum class PaymentMethod {
    ONLINE_GATEWAY, COD, WALLET
}

sealed class PaymentResult {
    data class Pending(
        val localOrderId: Int
    ) : PaymentResult()

    data class Redirect(
        val localOrderId: Int,
        val deeplink: String?,
        val qrImageUrl: String?,
        val orderCode: Long? = null
    ) : PaymentResult()

    data class Success(
        val orderId: Int,
        val status: PaymentStatus = PaymentStatus.SUCCESS,
        val serverOrderId: Int? = null,
        val providerRef: String? = null
    ) :
        PaymentResult()

    data class Failed(val orderID: Int, val reason: String) : PaymentResult()
    data class Processing(val orderId: Int) : PaymentResult()
}
