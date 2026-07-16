package com.example.easymart.presentation.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.easymart.presentation.ui.common.components.ItemProductRecommendCard
import com.example.easymart.presentation.ui.mock.mockProducts
import com.example.easymart.utils.toVNDString

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState = HomeUiState(),
    onAddToCart: (Product) -> Unit = {},
    onProductClick: (Product) -> Unit,
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(appColors.surfaceContainerLow)
    ) {
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Box
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.24f)
                .background(MaterialTheme.colorScheme.primary)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.96f)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = dimens.radiusXl, topEnd = dimens.radiusXl),
            colors = CardDefaults.cardColors(containerColor = appColors.surfaceContainerLow),
            elevation = CardDefaults.cardElevation(defaultElevation = dimens.cardElevation)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(dimens.screenPadding),
                verticalArrangement = Arrangement.spacedBy(dimens.spaceMd)
            ) {
                item {
                    HomePromoBanner()
                }

                uiState.error?.let { message ->
                    item {
                        HomeInfoBanner(message = message)
                    }
                }

                if (uiState.products.isEmpty()) {
                    item {
                        HomeEmptyState()
                    }
                } else {
                    item {
                        SectionHeader(
                            title = "Sản phẩm nổi bật",
                            subtitle = "Được đánh giá cao và đang bán tốt"
                        )
                    }

                    item {
                        FeaturedProductCard(
                            product = uiState.products.featuredProduct(),
                            onClick = onProductClick,
                            onAddToCart = onAddToCart
                        )
                    }

                    item {
                        SectionHeader(
                            title = "Gợi ý cho bạn",
                            subtitle = "${uiState.products.size} sản phẩm phù hợp"
                        )
                    }

                    items(uiState.products.chunked(2)) { rowProducts ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm)
                        ) {
                            rowProducts.forEach { product ->
                                ItemProductRecommendCard(
                                    product = product,
                                    onProductClick = onProductClick,
                                    onAddToCart = onAddToCart,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (rowProducts.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(dimens.spaceSm))
                    }
                }
            }
        }
    }
}

@Composable
private fun HomePromoBanner() {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = dimens.featuredCardHeight),
        shape = RoundedCornerShape(dimens.radiusLarge),
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = dimens.cardElevation
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            appColors.primaryGradientStart,
                            appColors.primaryGradientEnd
                        )
                    )
                )
                .padding(dimens.spaceLg)
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart),
                verticalArrangement = Arrangement.spacedBy(dimens.spaceSm)
            ) {
                Surface(
                    shape = RoundedCornerShape(dimens.radiusXl),
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.16f)
                ) {
                    Text(
                        text = "FLASH SALE",
                        modifier = Modifier.padding(
                            horizontal = dimens.spaceMd,
                            vertical = dimens.spaceXs
                        ),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Text(
                    text = "Ưu đãi hôm nay",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = "Săn deal tốt, giao nhanh và chọn sản phẩm còn hàng.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.88f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Surface(
                    shape = RoundedCornerShape(dimens.radiusMedium),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Text(
                        text = "Mua ngay",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(
                            horizontal = dimens.spaceLg,
                            vertical = dimens.spaceSm
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Surface(
                modifier = Modifier.align(Alignment.BottomEnd),
                shape = RoundedCornerShape(dimens.radiusLarge),
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.14f)
            ) {
                Column(
                    modifier = Modifier.padding(dimens.spaceMd),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "50%",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = "giảm tối đa",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.84f)
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(LocalAppDimens.current.spaceXs)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = LocalAppColors.current.textPrimary
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = LocalAppColors.current.textSecondary
        )
    }
}

@Composable
private fun FeaturedProductCard(
    product: Product,
    onClick: (Product) -> Unit,
    onAddToCart: (Product) -> Unit,
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current
    val inStock = product.stockQuantity > 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = dimens.featuredCardHeight),
        shape = RoundedCornerShape(dimens.radiusLarge),
        colors = CardDefaults.cardColors(containerColor = appColors.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.cardElevation),
        border = androidx.compose.foundation.BorderStroke(
            width = dimens.dividerThickness,
            color = appColors.outlineVariant
        ),
        onClick = { onClick(product) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimens.spaceMd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.spaceMd)
        ) {
            HomeProductImage(
                product = product,
                modifier = Modifier
                    .size(dimens.recommendedWidth)
                    .clip(RoundedCornerShape(dimens.radiusMedium))
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimens.spaceXs)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = appColors.textPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = product.priceVnd.toVNDString(),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                ProductMetaRow(product = product)
                StockLabel(inStock = inStock, stockQuantity = product.stockQuantity)

                Surface(
                    onClick = { if (inStock) onAddToCart(product) },
                    enabled = inStock,
                    shape = RoundedCornerShape(dimens.radiusMedium),
                    color = if (inStock) MaterialTheme.colorScheme.primary else appColors.disabledContainer,
                    contentColor = if (inStock) MaterialTheme.colorScheme.onPrimary else appColors.disabledContent
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = dimens.spaceMd,
                            vertical = dimens.spaceSm
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimens.spaceXs)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AddShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(dimens.iconSmall)
                        )
                        Text(
                            text = if (inStock) "Thêm vào giỏ" else "Tạm hết hàng",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeProductImage(
    product: Product,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current
    val isPreview = LocalInspectionMode.current

    Box(
        modifier = modifier.background(appColors.surfaceContainerHigh),
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
private fun ProductMetaRow(product: Product) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            modifier = Modifier.size(dimens.iconSmall),
            tint = appColors.warning
        )
        Text(
            text = if (product.rating.rate > 0) {
                "${product.rating.rate} (${product.rating.count})"
            } else {
                "Chưa có đánh giá"
            },
            style = MaterialTheme.typography.bodySmall,
            color = appColors.textSecondary,
            maxLines = 1
        )
        Text(
            text = "Đã bán ${product.soldQuantity}",
            style = MaterialTheme.typography.bodySmall,
            color = appColors.textSecondary,
            maxLines = 1
        )
    }
}

@Composable
private fun StockLabel(
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.spaceXs)
        ) {
            Icon(
                imageVector = Icons.Filled.Inventory2,
                contentDescription = null,
                modifier = Modifier.size(dimens.iconSmall)
            )
            Text(
                text = if (inStock) "Còn $stockQuantity sản phẩm" else "Hết hàng",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

@Composable
private fun HomeInfoBanner(message: String) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.radiusMedium),
        color = appColors.infoContainer
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(dimens.spaceMd),
            style = MaterialTheme.typography.bodyMedium,
            color = appColors.info
        )
    }
}

@Composable
private fun HomeEmptyState() {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.radiusLarge),
        color = appColors.surfaceContainer,
        border = androidx.compose.foundation.BorderStroke(
            width = dimens.dividerThickness,
            color = appColors.outlineVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(dimens.spaceXl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimens.spaceSm)
        ) {
            Text(
                text = "Chưa có sản phẩm",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = appColors.textPrimary
            )
            Text(
                text = "Danh sách sản phẩm sẽ xuất hiện tại đây sau khi đồng bộ.",
                style = MaterialTheme.typography.bodyMedium,
                color = appColors.textSecondary
            )
        }
    }
}

private fun List<Product>.featuredProduct(): Product {
    return sortedWith(
        compareByDescending<Product> { it.rating.rate }
            .thenByDescending { it.soldQuantity }
            .thenByDescending { it.stockQuantity }
    ).first()
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    EasyMartTheme {
        Surface {
            HomeScreen(
                uiState = HomeUiState(products = mockProducts),
                onAddToCart = {},
                onProductClick = {}
            )
        }
    }
}
