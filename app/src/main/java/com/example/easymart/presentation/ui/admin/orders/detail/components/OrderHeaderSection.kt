package com.example.easymart.presentation.ui.admin.orders.detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.Payment
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.utils.colorScheme
import com.example.easymart.utils.label
import com.example.easymart.utils.toShortLabel

@Composable
fun OrderHeaderSection(
    orderCode: String,
    createdDateText: String,
    orderStatus: OrderStatus,
    paymentStatus: PaymentStatus,
    paymentMethod: PaymentMethod,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        SectionTitle(
            icon = Icons.Rounded.ReceiptLong,
            title = "Thông tin đơn hàng"
        )

        OrderDetailInfoRow(
            label = "Mã đơn hàng:",
            value = orderCode,
            icon = Icons.Rounded.ReceiptLong,
            valueWeight = FontWeight.Bold
        )

        SectionDivider()

        OrderDetailInfoRow(
            label = "Ngày đặt hàng:",
            value = createdDateText,
            icon = Icons.Rounded.CalendarMonth
        )

        SectionDivider()

        OrderDetailInfoRow(
            label = "Trạng thái đơn hàng:",
            value = "",
            icon = Icons.Rounded.Inventory2,
            valueContent = {
                AdminOrderStatusChip(status = orderStatus)
            }
        )

        SectionDivider()

        OrderDetailInfoRow(
            label = "Trạng thái thanh toán:",
            value = "",
            icon = Icons.Rounded.Payment,
            valueContent = {
                AdminPaymentStatusChip(status = paymentStatus)
            }
        )

        SectionDivider()

        OrderDetailInfoRow(
            label = "Phương thức thanh toán:",
            value = "",
            icon = Icons.Rounded.AccountBalanceWallet,
            valueContent = {
                PaymentMethodChip(method = paymentMethod)
            }
        )
    }
}

@Composable
private fun PaymentMethodChip(
    method: PaymentMethod,
    modifier: Modifier = Modifier
) {
    val colorScheme = method.colorScheme()

    AssistChip(
        onClick = {},
        modifier = modifier,
        label = {
            Text(
                text = method.toShortLabel(),
                fontWeight = FontWeight.SemiBold
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = colorScheme.containerColor,
            labelColor = colorScheme.contentColor
        ),
        border = BorderStroke(
            width = 1.dp,
            color = colorScheme.borderColor
        )
    )
}

@Composable
internal fun SectionCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content = content
        )
    }
}

@Composable
internal fun SectionTitle(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
internal fun SectionDivider() {
    HorizontalDivider(
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.65f)
    )
}

@Preview(showBackground = true)
@Composable
private fun OrderHeaderSectionPreview() {
    EasyMartTheme {
        OrderHeaderSection(
            orderCode = "ORD-001245",
            createdDateText = "04/06/2026 • 10:24",
            orderStatus = OrderStatus.PROCESSING,
            paymentStatus = PaymentStatus.UNPAID,
            paymentMethod = PaymentMethod.COD,
            modifier = Modifier.padding(16.dp)
        )
    }
}