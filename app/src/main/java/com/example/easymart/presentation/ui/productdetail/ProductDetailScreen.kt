package com.example.easymart.presentation.ui.productdetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.easymart.R
import coil.compose.AsyncImage
import com.example.easymart.presentation.ui.common.components.RoundedActionButton
import com.example.easymart.presentation.ui.common.components.ItemProductRecommendCard
import com.example.easymart.presentation.ui.common.expansion.pressScale
import com.example.easymart.presentation.ui.mock.mockProducts
import com.example.easymart.utils.toVNDString

@Composable
fun ProductDetailScreen(
    modifier: Modifier = Modifier,
    uiState: ProductDetailUiState = ProductDetailUiState(),
    initialQuantity: Int = 1,
    onPlusClick: () -> Unit = {},
    onMinusClick: () -> Unit = {},
    onAddToCartClick: (Product, Int) -> Unit = { _, _ -> },
    onBuyNowClick: (Product, Int) -> Unit = { _, _ -> },
    onRecommendedProductClick: (Product) -> Unit = {},
) {
    val dimens = LocalAppDimens.current
    var quantity by remember { mutableIntStateOf(initialQuantity.coerceAtLeast(1)) }

    Box(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.product == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Không có dữ liệu sản phẩm")
                }
            }

            else -> {
                val product = uiState.product
                val similarProducts = uiState.similarProducts

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    contentPadding = PaddingValues(horizontal = dimens.spaceMd, vertical = dimens.spaceSm)
                ) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(dimens.productImageHeight),
                            shape = RoundedCornerShape(dimens.radiusLarge),
                            colors = cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            if (product.imageUrl.isNotBlank()) {
                                AsyncImage(
                                    model = product.imageUrl,
                                    contentDescription = product.name,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Inside,
                                    placeholder = painterResource(id = R.drawable.pic_shoe_1),
                                    error = painterResource(id = R.drawable.pic_shoe_1)
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = product.imageRes),
                                    contentDescription = product.name,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Inside
                                )
                            }
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = dimens.spaceSm),
                            colors = cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = dimens.spaceMd)
                            ) {
                                Text(
                                    text = product.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = dimens.spaceXs),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = product.category,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier
                                            .background(
                                                color = MaterialTheme.colorScheme.surfaceVariant,
                                                shape = RoundedCornerShape(dimens.radiusSmall)
                                            )
                                            .padding(
                                                horizontal = dimens.spaceSm,
                                                vertical = dimens.spaceXs
                                            )
                                    )

                                    Spacer(modifier = Modifier.width(dimens.spaceSm))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_star_filled),
                                            contentDescription = "rating",
                                            tint = MaterialTheme.colorScheme.tertiary,
                                            modifier = Modifier.size(dimens.contentIconSize)
                                        )
                                        Spacer(modifier = Modifier.width(dimens.spaceXs))
                                        Text(
                                            text = "${product.rating.rate}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = " (${product.rating.count})",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Text(
                                    text = product.price.toVNDString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(top = dimens.spaceSm)
                                )

                                Text(
                                    text = stringResource(id = R.string.label_description),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(top = dimens.spaceMd, bottom = dimens.spaceXs),
                                )

                                Text(
                                    text = product.description ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = dimens.spaceMd),
                                    verticalArrangement = Arrangement.spacedBy(dimens.spaceSm)
                                ) {
                                    // Quantity
                                    Column {
                                        Text(
                                            text = stringResource(id = R.string.label_quantity),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )

                                        Spacer(modifier = Modifier.height(dimens.spaceXs))

                                        Surface(
                                            shape = RoundedCornerShape(dimens.radiusMedium),
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            tonalElevation = 1.dp
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(
                                                    horizontal = dimens.spaceSm
                                                ),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                IconButton(
                                                    onClick = {
                                                        if (quantity > 1) {
                                                            quantity -= 1
                                                            onMinusClick()
                                                        }
                                                    },
                                                    modifier = Modifier.size(30.dp)
                                                ) {
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.ic_minus),
                                                        contentDescription = "minus",
                                                        tint = MaterialTheme.colorScheme.onSurface,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }

                                                Text(
                                                    text = quantity.toString(),
                                                    style = MaterialTheme.typography.titleSmall,
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    modifier = Modifier.padding(horizontal = dimens.spaceSm)
                                                )

                                                IconButton(
                                                    onClick = {
                                                        quantity += 1
                                                        onPlusClick()
                                                    },
                                                    modifier = Modifier.size(30.dp)
                                                ) {
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.ic_plus),
                                                        contentDescription = "plus",
                                                        tint = MaterialTheme.colorScheme.onSurface,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    // Actions
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        FilledTonalButton(
                                            onClick = { onAddToCartClick(product, quantity) },
                                            modifier = Modifier
                                                .weight(1f)
                                                .heightIn(min = 52.dp),
                                            shape = RoundedCornerShape(dimens.radiusMedium),
                                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp)
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_add_to_cart),
                                                contentDescription = stringResource(id = R.string.action_add_to_cart),
                                                modifier = Modifier.size(20.dp),
                                                tint = androidx.compose.ui.graphics.Color.Unspecified
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(text = stringResource(id = R.string.action_add_to_cart))
                                        }

                                        Button(
                                            onClick = { onBuyNowClick(product, quantity) },
                                            modifier = Modifier
                                                .weight(1f)
                                                .heightIn(min = 52.dp),
                                            shape = RoundedCornerShape(dimens.radiusMedium),
                                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp)
                                        ) {
                                            Text(text = stringResource(id = R.string.action_buy_now))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (similarProducts.isNotEmpty()) {
                        item {
                            Text(
                                text = stringResource(id = R.string.label_recommended_products),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(top = dimens.spaceMd, bottom = dimens.spaceSm)
                            )
                        }

                        item {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(bottom = dimens.spaceLg),
                                verticalArrangement = Arrangement.spacedBy(dimens.spaceSm),
                                horizontalArrangement = Arrangement.spacedBy(dimens.spaceMd),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 0.dp, max = 720.dp)
                            ) {
                                items(similarProducts) { similarProduct ->
                                    ItemProductRecommendCard(
                                        product = similarProduct,
                                        onProductClick = onRecommendedProductClick,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ProductDetailScreenPreview() {
    EasyMartTheme {
        ProductDetailScreen(
            uiState = ProductDetailUiState(
                product = mockProducts.firstOrNull(),
                similarProducts = mockProducts.drop(1).take(4)
            )
        )
    }
}