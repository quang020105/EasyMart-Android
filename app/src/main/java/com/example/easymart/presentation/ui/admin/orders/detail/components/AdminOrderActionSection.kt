package com.example.easymart.presentation.ui.admin.orders.detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.LocalShipping
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.utils.canAdminCancel
import com.example.easymart.utils.hasAdminAction
import com.example.easymart.utils.toAdminPrimaryActionString

@Composable
fun AdminOrderActionSection(
    orderStatus: OrderStatus,
    paymentStatus: PaymentStatus,
    refundAmountText: String?,
    isLoading: Boolean,
    onConfirmOrder: () -> Unit,
    onCancelOrder: () -> Unit,
    onMoveToProcessing: () -> Unit,
    onMoveToShipping: () -> Unit,
    onConfirmDelivered: () -> Unit,
    onConfirmRefund: () -> Unit,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        SectionTitle(icon = Icons.Rounded.Security, title = "Thao tác quản trị")
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            if (!orderStatus.hasAdminAction() && paymentStatus != PaymentStatus.REFUND_REQUIRED) {
                Text(
                    text = "Đơn hàng hiện không còn thao tác xử lý.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (orderStatus.hasAdminAction()) {
                orderStatus.toAdminPrimaryActionString()?.let { text ->
                    PrimaryActionButton(
                        text = text,
                        icon = orderStatus.toPrimaryActionIcon(),
                        isLoading = isLoading,
                        onClick = when (orderStatus) {
                            OrderStatus.CREATED -> onConfirmOrder
                            OrderStatus.CONFIRMED -> onMoveToProcessing
                            OrderStatus.PACKING -> onMoveToShipping
                            OrderStatus.SHIPPING -> onConfirmDelivered
                            OrderStatus.CANCELLATION_REQUESTED -> onCancelOrder
                            OrderStatus.DELIVERED, OrderStatus.CANCELLED -> ({})
                        }
                    )
                }

                if (orderStatus.canAdminCancel()) {
                    DangerActionButton(
                        text = if (orderStatus == OrderStatus.CANCELLATION_REQUESTED) {
                            "Duyệt hủy đơn"
                        } else {
                            "Hủy đơn"
                        },
                        icon = Icons.Rounded.Cancel,
                        enabled = !isLoading,
                        onClick = onCancelOrder
                    )
                }
            }

            if (paymentStatus == PaymentStatus.REFUND_REQUIRED) {
                PrimaryActionButton(
                    text = refundAmountText?.let { "Xác nhận đã hoàn $it" }
                        ?: "Xác nhận đã hoàn tiền",
                    icon = Icons.Rounded.CheckCircle,
                    isLoading = isLoading,
                    onClick = onConfirmRefund
                )
            }
        }
    }
}

private fun OrderStatus.toPrimaryActionIcon(): ImageVector = when (this) {
    OrderStatus.CANCELLATION_REQUESTED -> Icons.Rounded.Cancel
    OrderStatus.CREATED -> Icons.Rounded.Verified
    OrderStatus.CONFIRMED -> Icons.Rounded.Inventory2
    OrderStatus.PACKING -> Icons.Rounded.LocalShipping
    OrderStatus.SHIPPING -> Icons.Rounded.CheckCircle
    OrderStatus.DELIVERED, OrderStatus.CANCELLED -> Icons.Rounded.Security
}

@Composable
private fun PrimaryActionButton(
    text: String,
    icon: ImageVector,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Icon(imageVector = icon, contentDescription = null)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun DangerActionButton(
    text: String,
    icon: ImageVector,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(width = 1.dp, color = Color(0xFFDC2626))
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color(0xFFDC2626))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
    }
}

@Preview(showBackground = true)
@Composable
private fun AdminOrderActionSectionPreview() {
    EasyMartTheme {
        AdminOrderActionSection(
            orderStatus = OrderStatus.PACKING,
            paymentStatus = PaymentStatus.UNPAID,
            refundAmountText = null,
            isLoading = false,
            onConfirmOrder = {},
            onCancelOrder = {},
            onMoveToProcessing = {},
            onMoveToShipping = {},
            onConfirmDelivered = {},
            onConfirmRefund = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
