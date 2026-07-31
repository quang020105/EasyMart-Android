package com.example.easymart.presentation.ui.admin.orders.detail.components
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

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
            title = stringResource(R.string.ui_text_048)
        )

        PriceSummaryRow(
            label = stringResource(R.string.ui_text_049),
            value = subtotalText
        )

        PriceSummaryRow(
            label = stringResource(R.string.ui_text_050),
            value = shippingFeeText
        )

        if (!discountText.isNullOrBlank()) {
            PriceSummaryRow(
                label = stringResource(R.string.ui_text_051),
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
            text = stringResource(R.string.ui_text_052),
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
        subtotalText = stringResource(R.string.ui_text_351),
        shippingFeeText = stringResource(R.string.ui_text_352),
        discountText = stringResource(R.string.ui_text_353),
        totalText = stringResource(R.string.ui_text_354),
            modifier = Modifier.padding(16.dp)
        )
    }
}
