package com.example.easymart.presentation.ui.productdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.colors.LocalAppColors
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.common.components.ItemProductRecommendCard
import com.example.easymart.presentation.ui.mock.mockProducts
import com.example.easymart.presentation.ui.productdetail.components.DetailInfoBanner
import com.example.easymart.presentation.ui.productdetail.components.EmptyDetailState
import com.example.easymart.presentation.ui.productdetail.components.ProductDescriptionCard
import com.example.easymart.presentation.ui.productdetail.components.ProductDetailSectionHeader
import com.example.easymart.presentation.ui.productdetail.components.ProductHeroCard
import com.example.easymart.presentation.ui.productdetail.components.ProductPurchaseCard
import com.example.easymart.presentation.ui.productdetail.components.ProductSummaryCard
import com.example.easymart.presentation.ui.productdetail.components.SimilarLoadingCard

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
    val appColors = LocalAppColors.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(appColors.surfaceContainerLow)
    ) {
        when {
            uiState.isLoading || (uiState.product == null && uiState.isRefreshing) -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.product == null -> {
                EmptyDetailState(message = uiState.error ?: "Không có dữ liệu sản phẩm")
            }

            else -> {
                val product = uiState.product
                var quantity by remember(product.id) {
                    mutableIntStateOf(initialQuantity.coerceAtLeast(1))
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(dimens.screenPadding),
                    verticalArrangement = Arrangement.spacedBy(dimens.spaceMd)
                ) {
                    item {
                        ProductHeroCard(product = product)
                    }

                    uiState.refreshError?.let { message ->
                        item {
                            DetailInfoBanner(message = message)
                        }
                    }

                    item {
                        ProductSummaryCard(product = product)
                    }

                    item {
                        ProductDescriptionCard(description = product.description.orEmpty())
                    }

                    item {
                        ProductPurchaseCard(
                            product = product,
                            quantity = quantity,
                            inStock = product.stockQuantity > 0,
                            onMinusClick = {
                                if (quantity > 1) {
                                    quantity -= 1
                                    onMinusClick()
                                }
                            },
                            onPlusClick = {
                                if (quantity < product.stockQuantity) {
                                    quantity += 1
                                    onPlusClick()
                                }
                            },
                            onAddToCartClick = { onAddToCartClick(product, quantity) },
                            onBuyNowClick = { onBuyNowClick(product, quantity) }
                        )
                    }

                    if (uiState.isLoadingSimilar) {
                        item {
                            SimilarLoadingCard()
                        }
                    } else if (uiState.similarProducts.isNotEmpty()) {
                        item {
                            ProductDetailSectionHeader(
                                title = "Sản phẩm tương tự"
                            )
                        }

                        items(uiState.similarProducts.chunked(2)) { rowProducts ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm)
                            ) {
                                rowProducts.forEach { similarProduct ->
                                    ItemProductRecommendCard(
                                        product = similarProduct,
                                        onProductClick = onRecommendedProductClick,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                if (rowProducts.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
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

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    EasyMartTheme {
        Surface {
            ProductDetailScreen(
                uiState = ProductDetailUiState(
                    product = mockProducts.firstOrNull(),
                    similarProducts = mockProducts.drop(1).take(4)
                )
            )
        }
    }
}
