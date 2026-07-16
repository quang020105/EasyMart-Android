package com.example.easymart.presentation.ui.productdetail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.colors.LocalAppColors
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.mock.mockProducts
import com.example.easymart.utils.toVNDString

@Composable
internal fun ProductSummaryCard(
    product: Product,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current
    val inStock = product.stockQuantity > 0

    SurfaceCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(dimens.spaceMd),
            verticalArrangement = Arrangement.spacedBy(dimens.spaceMd)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(dimens.spaceXs)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = appColors.textPrimary,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = product.priceVnd.toVNDString(),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm)
            ) {
                DetailStatChip(
                    icon = Icons.Filled.Star,
                    title = if (product.rating.rate > 0) product.rating.rate.toString() else "Mới",
                    subtitle = "${product.rating.count} đánh giá",
                    modifier = Modifier.weight(1f)
                )
                DetailStatChip(
                    icon = Icons.Filled.ShoppingBag,
                    title = product.soldQuantity.toString(),
                    subtitle = "Đã bán",
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm)
            ) {
                StatusChip(
                    text = if (inStock) "Còn ${product.stockQuantity} sản phẩm" else "Hết hàng",
                    icon = if (inStock) Icons.Filled.Inventory2 else Icons.Filled.RemoveShoppingCart,
                    positive = inStock,
                    modifier = Modifier.weight(1f)
                )
                InfoChip(
                    text = "Chính hãng",
                    icon = Icons.Filled.Verified,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun DetailStatChip(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(dimens.radiusLarge),
        color = appColors.surfaceContainerHigh,
        border = BorderStroke(
            width = dimens.dividerThickness,
            color = appColors.outlineVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(dimens.spaceSm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm)
        ) {
            IconBadge(icon = icon)
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = appColors.textPrimary,
                    maxLines = 1
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelMedium,
                    color = appColors.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun StatusChip(
    text: String,
    icon: ImageVector,
    positive: Boolean,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(dimens.radiusLarge),
        color = if (positive) appColors.successContainer else appColors.warningContainer,
        contentColor = if (positive) appColors.success else appColors.onWarning
    ) {
        Row(
            modifier = Modifier.padding(dimens.spaceSm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.spaceXs)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(dimens.iconSmall)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductSummaryCardPreview() {
    EasyMartTheme {
        ProductSummaryCard(product = mockProducts.first())
    }
}
