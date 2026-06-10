package com.example.easymart.presentation.ui.admin.orders.management.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.LocalShipping
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Paid
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.ui.admin.orders.management.AdminOrderUiModel
import com.example.easymart.utils.colorScheme

@Composable
fun AdminOrderCard(
    order: AdminOrderUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OrderLeadingIcon(
                orderStatus = order.orderStatus
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1.15f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OrderInfoLine(
                    label = "Mã đơn:",
                    value = order.id,
                    valueColor = Color(0xFF2563EB),
                    valueFontWeight = FontWeight.Bold,
                    icon = Icons.Rounded.ReceiptLong
                )

                OrderInfoLine(
                    label = "Khách hàng:",
                    value = order.customerName,
                    icon = Icons.Rounded.Person
                )

                OrderInfoLine(
                    label = "SĐT:",
                    value = order.phoneNumber,
                    icon = Icons.Rounded.Phone
                )

                OrderInfoLine(
                    label = "Tổng tiền:",
                    value = order.totalAmountText,
                    valueColor = Color(0xFF2563EB),
                    valueFontWeight = FontWeight.Bold,
                    icon = Icons.Rounded.Paid
                )

                OrderInfoLine(
                    label = "Ngày đặt:",
                    value = order.createdDateText,
                    icon = Icons.Rounded.CalendarMonth
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            HorizontalDivider(
                modifier = Modifier
                    .height(86.dp)
                    .width(1.dp),
                color = Color(0xFFE2E8F0)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(0.95f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OrderStatusChip(
                    status = order.orderStatus
                )

                PaymentMethodChip(
                    method = order.paymentMethod
                )

                PaymentStatusChip(
                    status = order.paymentStatus
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = "Xem chi tiết",
                tint = Color(0xFF94A3B8)
            )
        }
    }
}

@Composable
private fun OrderLeadingIcon(
    orderStatus: OrderStatus,
    modifier: Modifier = Modifier
) {
    val icon = when (orderStatus) {
        OrderStatus.CREATED -> Icons.Rounded.ShoppingBag
        OrderStatus.CONFIRMED -> Icons.AutoMirrored.Rounded.ReceiptLong
        OrderStatus.PROCESSING -> Icons.Rounded.Inventory2
        OrderStatus.SHIPPING -> Icons.Rounded.LocalShipping
        OrderStatus.DELIVERED -> Icons.Rounded.CheckCircle
        OrderStatus.CANCELLED -> Icons.Rounded.Cancel
    }

    val scheme = orderStatus.colorScheme()

    Box(
        modifier = modifier
            .size(54.dp)
            .clip(CircleShape)
            .background(scheme.containerColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            tint = scheme.contentColor
        )
    }
}

@Composable
private fun OrderInfoLine(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    valueColor: Color = Color(0xFF0F172A),
    valueFontWeight: FontWeight = FontWeight.Medium
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(15.dp),
            tint = Color(0xFF64748B)
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF475569),
            maxLines = 1
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = valueFontWeight,
            color = valueColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun AdminOrderCardPreview() {
    EasyMartTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF8FAFC))
                .padding(16.dp)
        ) {
            AdminOrderCard(
                order = AdminOrderUiModel(
                    id = "ORD-001",
                    customerName = "Nguyễn Văn A",
                    phoneNumber = "0987654321",
                    totalAmountText = "450.000đ",
                    createdDateText = "04/06/2026",
                    orderStatus = OrderStatus.CREATED,
                    paymentMethod = PaymentMethod.COD,
                    paymentStatus = PaymentStatus.UNPAID
                ),
                onClick = {}
            )
        }
    }
}