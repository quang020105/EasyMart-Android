package com.example.easymart.presentation.ui.admin.orders.management

import androidx.compose.runtime.Immutable
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentStatus

@Immutable
data class AdminOrdersUiModel(
    val id: String,
    val customerName: String,
    val phoneNumber: String,
    val totalAmountText: String,
    val orderStatus: OrderStatus,
    val paymentMethod: PaymentMethod,
    val paymentStatus: PaymentStatus,
    val createdDateText: String
)