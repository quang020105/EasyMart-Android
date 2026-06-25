package com.example.easymart.presentation.ui.admin.orders

import com.example.easymart.domain.model.Order
import com.example.easymart.presentation.ui.admin.orders.detail.AdminOrderDetailUiModel
import com.example.easymart.presentation.ui.admin.orders.detail.components.OrderDetailItemUiModel
import com.example.easymart.presentation.ui.admin.orders.management.AdminOrdersUiModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val moneyFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("vi", "VN"))

fun Order.toAdminOrderUiModel(): AdminOrdersUiModel {
    return AdminOrdersUiModel(
        id = remoteId ?: id.toString(),
        customerName = shippingAddress.name.ifBlank { userId },
        phoneNumber = shippingAddress.phone,
        totalAmountText = moneyFormat.format(totalAmount),
        orderStatus = status,
        paymentMethod = paymentMethod,
        paymentStatus = paymentStatus,
        createdDateText = dateFormat.format(Date(createdAt))
    )
}

fun Order.toAdminOrderDetailUiModel(): AdminOrderDetailUiModel {
    val subtotal = items.sumOf { (it.product.price * it.quantity).toLong() }
    val shippingFee = (totalAmount - subtotal).coerceAtLeast(0L)

    return AdminOrderDetailUiModel(
        orderCode = orderNumber.ifBlank { remoteId ?: "ORD-$id" },
        createdDateText = dateFormat.format(Date(createdAt)),
        orderStatus = status,
        paymentStatus = paymentStatus,
        paymentMethod = paymentMethod,
        customerName = shippingAddress.name.ifBlank { userId },
        customerPhone = shippingAddress.phone,
        customerEmail = null,
        receiverName = shippingAddress.name,
        receiverPhone = shippingAddress.phone,
        addressDetail = shippingAddress.addressString,
        shippingNote = null,
        items = items.map { item ->
            val price = item.product.price
            OrderDetailItemUiModel(
                productName = item.product.name,
                imageUrl = item.product.imageUrl,
                priceAtPurchaseText = moneyFormat.format(price),
                quantityText = item.quantity.toString(),
                lineTotalText = moneyFormat.format(price * item.quantity)
            )
        },
        subtotalText = moneyFormat.format(subtotal),
        shippingFeeText = moneyFormat.format(shippingFee),
        discountText = null,
        totalText = moneyFormat.format(totalAmount)
    )
}
