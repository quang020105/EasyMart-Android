package com.example.easymart.domain.model

data class Payment(
    val id: Int,
    val orderId: Int,
    val method: PaymentMethod,
    val status: PaymentStatus,
    val amount: Double,
    val createdAt: String
)

enum class PaymentStatus {
    PENDING, SUCCESS, FAILED
}

enum class PaymentMethod {
    BANKING, CASH_ON_DELIVERY, WALLET
}
