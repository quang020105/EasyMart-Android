package com.example.easymart.presentation.ui.admin.products.management.components
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

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
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.presentation.theme.EasyMartTheme

@Composable
fun SummaryRow(
    total: Int,
    visibleCount: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFF4F8FF),
                            Color(0xFFFFFFFF),
                            Color(0xFFF3FBF6)
                        )
                    )
                )
                .padding(22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SummaryMetric(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.Inventory2,
                iconColor = Color(0xFF2563EB),
                iconBackground = Color(0xFFEAF2FF),
                title = stringResource(R.string.ui_text_181),
                value = total.toString(),
                valueColor = Color(0xFF2563EB),
                subtitle = stringResource(R.string.ui_text_182)
            )

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(76.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.8f))
            )
            Spacer(modifier = Modifier.width(16.dp))

            SummaryMetric(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.Visibility,
                iconColor = Color(0xFF16A34A),
                iconBackground = Color(0xFFEAF8EF),
                title = stringResource(R.string.ui_text_183),
                value = visibleCount.toString(),
                valueColor = Color(0xFF16A34A),
                subtitle = stringResource(R.string.ui_text_182)
            )
        }
    }
}

@Composable
private fun SummaryMetric(
    icon: ImageVector,
    iconColor: Color,
    iconBackground: Color,
    title: String,
    value: String,
    valueColor: Color,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(iconBackground, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = valueColor
                )
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SummaryRowPreview() {
    EasyMartTheme {
        SummaryRow(total = 27, visibleCount = 27)
    }
}
