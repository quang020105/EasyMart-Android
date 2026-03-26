package com.example.easymart.data.remote.dto.payment

import com.example.easymart.data.remote.dto.PayOsItemDto

data class PayOsCreatePaymentRequest(
    val amount: Long,
    val description: String,
    val localOrderId: Long,
    val items: List<PayOsItemDto>  // thông tin cơ bản về sản phẩm truyền qua PayOS
)
