package com.example.easymart.presentation.ui.admin.products.detail.components
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.ui.mock.mockSimpleProduct

@Composable
fun ProductStatisticCard(
    product: Product,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row {
                Icon(
                    imageVector = Icons.Filled.BarChart,
                    contentDescription = null,
                    tint = Color(0xFF2563EB)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = stringResource(R.string.ui_text_158),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.padding(top = 14.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProductInfoTile(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Star,
                        iconColor = Color(0xFFFFB020),
                        iconBackground = Color(0xFFFFF7E6),
                        label = stringResource(R.string.ui_text_159),
                        value = product.rating.rate.toString(),
                        valueColor = MaterialTheme.colorScheme.onSurface
                    )

                    ProductInfoTile(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.ChatBubbleOutline,
                        iconColor = Color(0xFF2563EB),
                        iconBackground = Color(0xFFEAF2FF),
                        label = stringResource(R.string.ui_text_160),
                        value = product.rating.count.toString()
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProductInfoTile(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.CalendarMonth,
                        iconColor = Color(0xFF16A34A),
                        iconBackground = Color(0xFFEAF8EF),
                        label = stringResource(R.string.ui_text_161),
                        value = formatProductDate(product.createdAt),
                        valueColor = MaterialTheme.colorScheme.onSurface
                    )

                    ProductInfoTile(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.CalendarMonth,
                        iconColor = Color(0xFF7C3AED),
                        iconBackground = Color(0xFFF3E8FF),
                        label = stringResource(R.string.ui_text_162),
                        value = formatProductDate(product.updatedAt),
                        valueColor = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ProductStatisticCardPreview() {
    EasyMartTheme {
        ProductStatisticCard(
            product = mockSimpleProduct
        )
    }
}
