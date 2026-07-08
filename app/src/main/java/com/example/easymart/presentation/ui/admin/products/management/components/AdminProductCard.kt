package com.example.easymart.presentation.ui.admin.products.management.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.admin.products.management.formatPrice
import com.example.easymart.presentation.ui.mock.mockSimpleProduct
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminProductCard(
    product: Product,
    onEdit: () -> Unit,
    onImport: () -> Unit,
    onToggleVisibility: (Boolean) -> Unit,
    onViewDetail: () -> Unit
) {
    val dimens = LocalAppDimens.current
    val isFromApi = product.storagePath.isNullOrBlank() && product.localImageUri.isNullOrBlank()
    val statusLabel = if (isFromApi) "Có sẵn từ API" else "Đã thêm"
    val statusColor = if (isFromApi) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onViewDetail() },
        shape = RoundedCornerShape(dimens.radiusLarge),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.cardElevation)
    ) {
        Column(modifier = Modifier.padding(dimens.spaceMd)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(dimens.radiusMedium))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.width(dimens.spaceMd))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = product.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = product.brand.ifBlank { "Chưa có thương hiệu" },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = formatPrice(product.price),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(dimens.spaceXs))

                    Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(statusColor)
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = statusLabel, style = MaterialTheme.typography.labelSmall)
                        }

                    Text(
                        text = "Cập nhật: ${formatDate(product.updatedAt)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    StockChip(
                        isVisible = product.isVisible,
                    )
                    Spacer(modifier = Modifier.height(dimens.spaceXs))
                    Switch(checked = product.isVisible, onCheckedChange = onToggleVisibility)
                }
            }

            Spacer(modifier = Modifier.height(dimens.spaceSm))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                if (isFromApi) {
                    OutlinedButton(
                        onClick = onImport,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Filled.SouthWest, contentDescription = null)
                        Spacer(modifier = Modifier.width(dimens.spaceXs))
                        Text("Import")
                    }
                    Spacer(modifier = Modifier.width(dimens.spaceXs))
                } else {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Filled.Edit, contentDescription = "Sửa")
                    }
                }
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    if (timestamp <= 0L) return "--/--/----"
    val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return date.format(Date(timestamp))
}

@Preview
@Composable
fun AdminProductCardPreview() {
    EasyMartTheme {
        AdminProductCard(
            product = mockSimpleProduct,
            onEdit = {},
            onImport = {},
            onToggleVisibility = {},
            onViewDetail = {}
        )
    }
}
