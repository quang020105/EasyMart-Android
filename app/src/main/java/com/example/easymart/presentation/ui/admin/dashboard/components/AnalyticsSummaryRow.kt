package com.example.easymart.presentation.ui.admin.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.presentation.theme.EasyMartTheme

@Composable
fun AnalyticsSummaryRow(
    title: String,
    subtitle: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    iconContainerColor: Color,
    valueColor: Color,
    sparklineColor: Color,
    sparklineValues: List<Float>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(iconContainerColor, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF101828)
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF667085)
                )
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF101828)
            )
        )

        Spacer(modifier = Modifier.width(14.dp))

        SparklineChart(
            values = sparklineValues,
            color = sparklineColor,
            modifier = Modifier
                .width(72.dp)
                .height(30.dp)
        )
    }
}

@Preview
@Composable
fun AnalyticsSummaryRowPreview() {
    EasyMartTheme {
        AnalyticsSummaryRow(
            title = "Doanh thu",
            subtitle = "Tháng này",
            value = "₫120.000.000",
            icon = Icons.Filled.AttachMoney,
            iconTint = Color(0xFF4CAF50),
            iconContainerColor = Color(0xFFE8F5E9),
            valueColor = Color(0xFF101828),
            sparklineColor = Color(0xFF4CAF50),
            sparklineValues = listOf(100f, 120f, 150f, 130f, 170f, 100f, 180f)
        )
    }
}
