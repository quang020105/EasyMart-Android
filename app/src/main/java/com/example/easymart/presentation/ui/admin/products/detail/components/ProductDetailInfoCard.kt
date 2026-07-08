package com.example.easymart.presentation.ui.admin.products.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
fun ProductDetailInfoCard(
    product: Product,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Thông tin sản phẩm",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 14.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProductInfoTile(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.LocalOffer,
                        iconColor = Color(0xFF2563EB),
                        iconBackground = Color(0xFFEAF2FF),
                        label = "Mã sản phẩm",
                        value = "#${product.id.toString().padStart(4, '0')}"
                    )

                    ProductInfoTile(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Category,
                        iconColor = Color(0xFF2563EB),
                        iconBackground = Color(0xFFEAF2FF),
                        label = "Danh mục",
                        value = product.category.ifBlank { "Chưa có" }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProductInfoTile(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.LocalOffer,
                        iconColor = Color(0xFF2563EB),
                        iconBackground = Color(0xFFEAF2FF),
                        label = "Thương hiệu",
                        value = product.brand.ifBlank { "Chưa có" }
                    )

                    androidx.compose.foundation.layout.Spacer(modifier = Modifier.weight(1f))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProductInfoTile(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Inventory2,
                        iconColor = Color(0xFFD97706),
                        iconBackground = Color(0xFFFFF7E6),
                        label = "Tồn kho",
                        value = product.stockQuantity.toString()
                    )

                    ProductInfoTile(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.RemoveShoppingCart,
                        iconColor = Color(0xFF7C3AED),
                        iconBackground = Color(0xFFF3E8FF),
                        label = "Đã bán",
                        value = product.soldQuantity.toString()
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProductInfoTile(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Sync,
                        iconColor = if (product.isSynced) Color(0xFF16A34A) else Color(0xFFDC2626),
                        iconBackground = if (product.isSynced) Color(0xFFEAF8EF) else Color(0xFFFFF1F2),
                        label = "Đồng bộ",
                        value = if (product.isSynced) "Đã đồng bộ" else "Chưa đồng bộ",
                        valueColor = if (product.isSynced) Color(0xFF16A34A) else Color(0xFFDC2626)
                    )

                    ProductInfoTile(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Visibility,
                        iconColor = if (product.isVisible) Color(0xFF16A34A) else Color(0xFFDC2626),
                        iconBackground = if (product.isVisible) Color(0xFFEAF8EF) else Color(0xFFFFF1F2),
                        label = "Trạng thái",
                        value = if (product.isVisible) "Đang hiển thị" else "Đang ẩn",
                        valueColor = if (product.isVisible) Color(0xFF16A34A) else Color(0xFFDC2626)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ProductDetailInfoCardPreview() {
    EasyMartTheme {
        ProductDetailInfoCard(
            product = mockSimpleProduct
        )
    }
}
