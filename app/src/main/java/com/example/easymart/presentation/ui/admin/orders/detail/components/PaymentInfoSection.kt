package com.example.easymart.presentation.ui.admin.orders.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Payment
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.utils.label

@Composable
fun PaymentInfoSection(
    paymentMethod: PaymentMethod,
    paymentStatus: PaymentStatus,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        SectionTitle(
            icon = Icons.Rounded.Payment,
            title = "Thông tin thanh toán"
        )

        OrderDetailInfoRow(
            label = "Phương thức:",
            value = paymentMethod.label(),
            icon = Icons.Rounded.Payment
        )

        SectionDivider()

        OrderDetailInfoRow(
            label = "Trạng thái:",
            value = paymentStatus.label(),
            icon = Icons.Rounded.WarningAmber
        )

        if (paymentMethod == PaymentMethod.COD) {
            CodPaymentNote()
        }
    }
}

@Composable
private fun CodPaymentNote(
    modifier: Modifier = Modifier
) {
    val primary = MaterialTheme.colorScheme.primary

    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(14.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Rounded.Info,
                contentDescription = null,
                tint = primary
            )

            Text(
                text = buildAnnotatedString {
                    append("Với đơn COD, khi xác nhận đã giao, hệ thống tự cập nhật thanh toán thành ")
                    withStyle(
                        style = SpanStyle(
                            color = primary,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Đã thanh toán.")
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PaymentInfoSectionPreview() {
    EasyMartTheme {
        PaymentInfoSection(
            paymentMethod = PaymentMethod.COD,
            paymentStatus = PaymentStatus.UNPAID,
            modifier = Modifier.padding(16.dp)
        )
    }
}