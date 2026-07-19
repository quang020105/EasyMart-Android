package com.example.easymart.presentation.ui.admin.orders.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.ui.admin.orders.detail.components.AdminOrderActionSection
import com.example.easymart.presentation.ui.admin.orders.detail.components.CustomerInfoSection
import com.example.easymart.presentation.ui.admin.orders.detail.components.OrderDetailItemUiModel
import com.example.easymart.presentation.ui.admin.orders.detail.components.OrderHeaderSection
import com.example.easymart.presentation.ui.admin.orders.detail.components.OrderItemsSection
import com.example.easymart.presentation.ui.admin.orders.detail.components.OrderPriceSummarySection
import com.example.easymart.presentation.ui.admin.orders.detail.components.PaymentInfoSection
import com.example.easymart.presentation.ui.admin.orders.detail.components.ShippingInfoSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrderDetailScreen(
    order: AdminOrderDetailUiModel,
    isActionLoading: Boolean,
    onConfirmOrder: () -> Unit,
    onCancelOrder: () -> Unit,
    onMoveToProcessing: () -> Unit,
    onMoveToShipping: () -> Unit,
    onConfirmDelivered: () -> Unit,
    onConfirmRefund: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 12.dp,
            end = 16.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            OrderHeaderSection(
                orderCode = order.orderCode,
                createdDateText = order.createdDateText,
                orderStatus = order.orderStatus,
                paymentStatus = order.paymentStatus,
                paymentMethod = order.paymentMethod
            )
        }

        item {
            CustomerInfoSection(
                customerName = order.customerName,
                phoneNumber = order.customerPhone,
                email = order.customerEmail
            )
        }

        item {
            ShippingInfoSection(
                receiverName = order.receiverName,
                receiverPhone = order.receiverPhone,
                addressDetail = order.addressDetail,
                shippingNote = order.shippingNote
            )
        }

        item {
            PaymentInfoSection(
                paymentMethod = order.paymentMethod,
                paymentStatus = order.paymentStatus,
                refundAmountText = order.refundAmountText
            )
        }

        item {
            OrderItemsSection(
                items = order.items
            )
        }

        item {
            OrderPriceSummarySection(
                subtotalText = order.subtotalText,
                shippingFeeText = order.shippingFeeText,
                discountText = order.discountText,
                totalText = order.totalText
            )
        }

        item {
            AdminOrderActionSection(
                orderStatus = order.orderStatus,
                paymentStatus = order.paymentStatus,
                refundAmountText = order.refundAmountText,
                isLoading = isActionLoading,
                onConfirmOrder = onConfirmOrder,
                onCancelOrder = onCancelOrder,
                onMoveToProcessing = onMoveToProcessing,
                onMoveToShipping = onMoveToShipping,
                onConfirmDelivered = onConfirmDelivered,
                onConfirmRefund = onConfirmRefund
            )
        }
    }
}

@Immutable
data class AdminOrderDetailUiModel(
    val orderCode: String,
    val createdDateText: String,
    val orderStatus: OrderStatus,
    val paymentStatus: PaymentStatus,
    val paymentMethod: PaymentMethod,

    val customerName: String,
    val customerPhone: String,
    val customerEmail: String?,

    val receiverName: String,
    val receiverPhone: String,
    val addressDetail: String,
    val shippingNote: String?,

    val items: List<OrderDetailItemUiModel>,

    val subtotalText: String,
    val shippingFeeText: String,
    val discountText: String?,
    val totalText: String,
    val cancellationReason: String? = null,
    val refundAmountText: String? = null
)

@Preview(showBackground = true)
@Composable
private fun AdminOrderDetailScreenPreview() {
    EasyMartTheme {
        AdminOrderDetailScreen(
            order = AdminOrderDetailUiModel(
                orderCode = "ORD-001245",
                createdDateText = "04/06/2026 • 10:24",
                orderStatus = OrderStatus.PACKING,
                paymentStatus = PaymentStatus.UNPAID,
                paymentMethod = PaymentMethod.COD,
                customerName = "Nguyễn Văn A",
                customerPhone = "0987 654 321",
                customerEmail = "nguyenvana@gmail.com",
                receiverName = "Nguyễn Văn A",
                receiverPhone = "0987 654 321",
                addressDetail = "Số 12, ngõ 45, Phường Dịch Vọng Hậu, Cầu Giấy, Hà Nội",
                shippingNote = "Giao giờ hành chính, gọi trước khi giao",
                items = listOf(
                    OrderDetailItemUiModel(
                        productName = "Tai nghe Bluetooth Pro X1",
                        imageUrl = null,
                        priceAtPurchaseText = "350.000đ",
                        quantityText = "1",
                        lineTotalText = "350.000đ"
                    ),
                    OrderDetailItemUiModel(
                        productName = "Bàn phím cơ Mini K68",
                        imageUrl = null,
                        priceAtPurchaseText = "420.000đ",
                        quantityText = "1",
                        lineTotalText = "420.000đ"
                    )
                ),
                subtotalText = "770.000đ",
                shippingFeeText = "30.000đ",
                discountText = "-50.000đ",
                totalText = "750.000đ"
            ),
            isActionLoading = false,
            onConfirmOrder = {},
            onCancelOrder = {},
            onMoveToProcessing = {},
            onMoveToShipping = {},
            onConfirmDelivered = {},
            onConfirmRefund = {}
        )
    }
}
