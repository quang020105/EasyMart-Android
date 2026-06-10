package com.example.easymart.presentation.ui.admin.orders.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Paid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.presentation.theme.EasyMartTheme

@Composable
fun OrderPriceSummarySection(
    subtotalText: String,
    shippingFeeText: String,
    discountText: String?,
    totalText: String,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        SectionTitle(
            icon = Icons.Rounded.Paid,
            title = "Tổng kết tiền"
        )

        PriceSummaryRow(
            label = "Tạm tính:",
            value = subtotalText
        )

        PriceSummaryRow(
            label = "Phí vận chuyển:",
            value = shippingFeeText
        )

        if (!discountText.isNullOrBlank()) {
            PriceSummaryRow(
                label = "Giảm giá:",
                value = discountText,
                valueColor = Color(0xFFDC2626)
            )
        }

        TotalPaymentRow(
            totalText = totalText
        )
    }
}

@Composable
private fun PriceSummaryRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = valueColor
        )
    }
}

@Composable
private fun TotalPaymentRow(
    totalText: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(
                horizontal = 14.dp,
                vertical = 12.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Tổng thanh toán:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = totalText,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OrderPriceSummarySectionPreview() {
    EasyMartTheme {
        OrderPriceSummarySection(
            subtotalText = "770.000đ",
            shippingFeeText = "30.000đ",
            discountText = "-50.000đ",
            totalText = "750.000đ",
            modifier = Modifier.padding(16.dp)
        )
    }
}