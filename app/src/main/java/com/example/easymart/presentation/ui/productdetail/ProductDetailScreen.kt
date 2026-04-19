package com.example.easymart.presentation.ui.productdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.foundation.layout.*
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
                    contentPadding = PaddingValues(horizontal = dimens.spaceMd)
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(dimens.productImageHeight)
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
                            modifier = Modifier.fillMaxWidth(),
                            colors = cardColors(containerColor = MaterialTheme.colorScheme.background)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = dimens.spaceMd)
                            ) {
                                Text(
                                    text = product.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Text(
                                    text = product.price.toVNDString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(top = dimens.spaceXs)
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

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .border(
                                                width = 0.dp,
                                                color = MaterialTheme.colorScheme.outline,
                                                shape = RoundedCornerShape(dimens.radiusSmall)
                                            ),
                                        shape = RoundedCornerShape(dimens.radiusSmall),
                                        color = MaterialTheme.colorScheme.surfaceVariant
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(
                                                horizontal = dimens.spaceSm,
                                                vertical = dimens.spaceXs
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
                                                modifier = Modifier.size(dimens.qtyBtnSize)
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_minus),
                                                    contentDescription = "minus",
                                                    tint = MaterialTheme.colorScheme.onSurface,
                                                    modifier = Modifier.size(dimens.contentIconSize)
                                                )
                                            }

                                            Text(
                                                text = quantity.toString(),
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.padding(horizontal = dimens.spaceSm)
                                            )

                                            IconButton(
                                                onClick = {
                                                    quantity += 1
                                                    onPlusClick()
                                                },
                                                modifier = Modifier.size(dimens.qtyBtnSize)
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_plus),
                                                    contentDescription = "plus",
                                                    tint = MaterialTheme.colorScheme.onSurface,
                                                    modifier = Modifier.size(dimens.contentIconSize)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.weight(1f))

                                    RoundedActionButton(
                                        text = stringResource(id = R.string.action_add_to_cart),
                                        onClick = { onAddToCartClick(product, quantity) },
                                        horizontalPadding = dimens.spaceMd
                                    )
                                }
                            }
                        }
                    }

                    item {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(dimens.spaceSm),
                            verticalArrangement = Arrangement.spacedBy(dimens.spaceSm),
                            horizontalArrangement = Arrangement.spacedBy(dimens.spaceMd),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1000.dp)
                        ) {
                            items(similarProducts) { similarProduct ->
                                ItemProductRecommendCard(
                                    product = similarProduct,
                                    onProductClick = {},
                                )
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