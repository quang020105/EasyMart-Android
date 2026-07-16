package com.example.easymart.presentation.ui.admin.products.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProductVisibilityBadge(
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    val container = if (isVisible) Color(0xFFEAF8EF) else Color(0xFFFFF1F2)
    val content = if (isVisible) Color(0xFF16A34A) else Color(0xFFDC2626)

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(999.dp),
        color = container
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                contentDescription = null,
                tint = content,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (isVisible) "Hiển thị" else "Đang ẩn",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = content
                )
            )
        }
    }
}

@Composable
fun ProductSourceBadge(
    product: Product,
    modifier: Modifier = Modifier
) {
    val isFromApi = product.storagePath.isNullOrBlank() && product.localImageUri.isNullOrBlank()

    AssistChip(
        modifier = modifier,
        onClick = {},
        label = {
            Text(
                text = if (isFromApi) "Có sẵn từ API" else "Admin thêm",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.CloudQueue,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (isFromApi) Color(0xFFEAF2FF) else Color(0xFFF0FDF4),
            labelColor = if (isFromApi) Color(0xFF2563EB) else Color(0xFF16A34A),
            leadingIconContentColor = if (isFromApi) Color(0xFF2563EB) else Color(0xFF16A34A)
        ),
        border = null
    )
}

@Composable
fun ProductInfoTile(
    icon: ImageVector,
    iconColor: Color,
    iconBackground: Color,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = Color(0xFF2563EB)
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(iconBackground, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = value,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = valueColor
                    )
                )
            }
        }
    }
}

fun formatProductPrice(price: Long): String {
    return "%,.0f đ".format(price)
}

fun formatProductDate(timestamp: Long): String {
    if (timestamp <= 0L) return "--/--/----"
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        .format(Date(timestamp))
}


@Preview
@Composable
fun ProductInfoTilePreview() {
    EasyMartTheme {
        ProductInfoTile(
            icon = Icons.Filled.Visibility,
            iconColor = Color(0xFF16A34A),
            iconBackground = Color(0xFFEAF8EF),
            label = "Trạng thái",
            value = "Hiển thị"
        )
    }
}
