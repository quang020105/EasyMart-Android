package com.example.easymart.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.R
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.common.components.ItemProductRecommendCard
import com.example.easymart.presentation.ui.common.components.ProductCard
import com.example.easymart.presentation.ui.home.components.ProductRow
import com.example.easymart.presentation.ui.mock.mockProducts
import com.example.easymart.presentation.ui.mock.mockSimpleProduct
import com.example.easymart.utils.toVNDString


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState = HomeUiState(),
    onAddToCart: (Product) -> Unit = {},
    onProductClick: (Product) -> Unit,
) {
    val dimens = LocalAppDimens.current
    Box(modifier = modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            // Loading UI
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.95f)
                    .background(color = Color.Transparent)
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(topStart = dimens.radiusXl, topEnd = dimens.radiusXl),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = dimens.screenPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    HomePromoBanner()
                    Spacer(modifier = Modifier.height(dimens.spaceMd))
                    Text(
                        text = "Sản phẩm nổi bật",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(dimens.spaceSm))
                    val featuredProduct = uiState.products.randomOrNull() ?: mockSimpleProduct
                    FeaturedProductCard(
                        product = featuredProduct,
                        onClick = onProductClick,
                        onAddToCart = onAddToCart
                    )
                    Spacer(modifier = Modifier.height(dimens.spaceMd))
                    Text(
                        text = "Gợi ý cho bạn",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(dimens.spaceSm))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(520.dp),
                        horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm),
                        verticalArrangement = Arrangement.spacedBy(dimens.spaceSm),
                        userScrollEnabled = false
                    ) {
                        items(uiState.products) { product ->
                            ItemProductRecommendCard(
                                product = product,
                                onProductClick = onProductClick,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomePromoBanner() {
    val dimens = LocalAppDimens.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(dimens.radiusLarge),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimens.spaceLg, vertical = dimens.spaceMd),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Flash Sale",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "Giảm giá lên đến 50%",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Text(
                    text = "Mua ngay",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(
                        horizontal = dimens.spaceMd,
                        vertical = dimens.spaceXs
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun FeaturedProductCard(
    product: Product,
    onClick: (Product) -> Unit,
    onAddToCart: (Product) -> Unit,
) {
    val dimens = LocalAppDimens.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(dimens.radiusMedium),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        onClick = { onClick(product) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimens.spaceSm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductCard(
                product = product,
                modifier = Modifier.size(100.dp),
                cornerRadius = dimens.radiusSmall,
                colorBackground = MaterialTheme.colorScheme.surfaceVariant,
                onClick = onClick
            )
            Spacer(modifier = Modifier.width(dimens.spaceSm))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(dimens.spaceXs))
                Text(
                    text = product.price.toVNDString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(dimens.spaceSm))
                Surface(
                    shape = RoundedCornerShape(dimens.radiusSmall),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = "Mua ngay",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(
                            horizontal = dimens.spaceMd,
                            vertical = dimens.spaceXs
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val fakeProducts = listOf(
        Product(1, "Giày sneakers", "Description 1", 40.55, "", R.drawable.pic_shoe_1),
        Product(2, "Product 2", "Description 2", 20.0, "", R.drawable.pic_shoe_1),
        Product(3, "Product 3", "Description 3", 30.0, "", R.drawable.pic_shoe_1)
    )

    EasyMartTheme {
        Surface {
            HomeScreen(
                onAddToCart = {},
                onProductClick = {}
            )
        }
    }
}