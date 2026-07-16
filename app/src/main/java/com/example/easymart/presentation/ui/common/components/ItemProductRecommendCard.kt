package com.example.easymart.presentation.ui.common.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import com.example.easymart.R
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.colors.LocalAppColors
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.utils.toVNDString

@Composable
fun ItemProductRecommendCard(
    product: Product,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier,
    onAddToCart: ((Product) -> Unit)? = null
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current
    val inStock = product.stockQuantity > 0

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.radiusLarge),
        colors = CardDefaults.cardColors(containerColor = appColors.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.cardElevation),
        border = BorderStroke(
            width = dimens.dividerThickness,
            color = appColors.outlineVariant
        ),
        onClick = { onProductClick(product) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.spaceSm),
            verticalArrangement = Arrangement.spacedBy(dimens.spaceSm)
        ) {
            ProductImagePanel(product = product)

            Text(
                text = product.name,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = appColors.textPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = product.priceVnd.toVNDString(),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            ProductCompactMeta(product = product)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StockCompactChip(
                    inStock = inStock,
                    stockQuantity = product.stockQuantity
                )

                onAddToCart?.let { addToCart ->
                    Surface(
                        onClick = { if (inStock) addToCart(product) },
                        enabled = inStock,
                        shape = RoundedCornerShape(dimens.radiusMedium),
                        color = if (inStock) MaterialTheme.colorScheme.primary else appColors.disabledContainer,
                        contentColor = if (inStock) MaterialTheme.colorScheme.onPrimary else appColors.disabledContent
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AddShoppingCart,
                            contentDescription = "Thêm vào giỏ hàng",
                            modifier = Modifier
                                .padding(dimens.spaceSm)
                                .size(dimens.iconSmall)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductImagePanel(product: Product) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current
    val isPreview = LocalInspectionMode.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimens.recommendedHeight)
            .clip(RoundedCornerShape(dimens.radiusMedium))
            .background(appColors.surfaceContainerHigh),
        contentAlignment = Alignment.Center
    ) {
        if (isPreview && product.imageRes != 0) {
            Image(
                painter = painterResource(product.imageRes),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimens.spaceSm),
                contentScale = ContentScale.Fit
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.imageUrl)
                    .crossfade(true)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .size(Size.ORIGINAL)
                    .build(),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimens.spaceSm),
                contentScale = ContentScale.Fit,
                error = painterResource(R.drawable.pic_shoe_1)
            )
        }
    }
}

@Composable
private fun ProductCompactMeta(product: Product) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.spaceXs)
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                modifier = Modifier.size(dimens.iconSmall),
                tint = appColors.warning
            )
            Text(
                text = if (product.rating.rate > 0) product.rating.rate.toString() else "Mới",
                style = MaterialTheme.typography.labelMedium,
                color = appColors.textSecondary,
                maxLines = 1
            )
        }

        Text(
            text = "Đã bán ${product.soldQuantity}",
            style = MaterialTheme.typography.labelMedium,
            color = appColors.textSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun StockCompactChip(
    inStock: Boolean,
    stockQuantity: Int
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    Surface(
        shape = RoundedCornerShape(dimens.radiusXl),
        color = if (inStock) appColors.successContainer else appColors.warningContainer,
        contentColor = if (inStock) appColors.success else appColors.onWarning
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = dimens.spaceSm,
                vertical = dimens.spaceXs
            ),
            horizontalArrangement = Arrangement.spacedBy(dimens.spaceXs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Inventory2,
                contentDescription = null,
                modifier = Modifier.size(dimens.iconSmall)
            )
            Text(
                text = if (inStock) "Còn $stockQuantity" else "Hết",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemProductRecommendCardPreview() {
    EasyMartTheme {
        ItemProductRecommendCard(
            product = Product(
                id = 1,
                name = "Giày sneaker cao cấp phối màu trắng xanh",
                priceVnd = 249_000L,
                imageRes = R.drawable.pic_shoe_1,
                description = "This is a sample product description.",
                imageUrl = "",
                stockQuantity = 12,
                soldQuantity = 15
            ),
            onProductClick = {},
            onAddToCart = {}
        )
    }
}
