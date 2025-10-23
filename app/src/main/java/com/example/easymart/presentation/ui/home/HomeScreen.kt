package com.example.easymart.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.R
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.common.components.ProductCard
import com.example.easymart.presentation.ui.common.components.ItemProductRecommendCard
import com.example.easymart.presentation.ui.home.components.ProductRow


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    productRecommends: List<Product> = emptyList(),
    onAddToCart: (Product) -> Unit = {},
    onProductClick: (Product) -> Unit,
) {
    val dimens = LocalAppDimens.current
    Box(modifier = modifier.fillMaxSize()){
        Box(modifier = modifier.fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
        )
        Card (modifier = Modifier.fillMaxWidth()
            .fillMaxHeight(0.9f)
            .background(color = Color.Transparent)
            .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = dimens.radiusXl, topEnd = dimens.radiusXl),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ){
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        all = dimens.screenPadding
                    )
                    .verticalScroll(rememberScrollState())
            ){
                Text(
                    text = "Sản phẩm nổi bật",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(dimens.spaceSm))
                val featuredProduct = productRecommends.first()
//                FeatureProductCard(
//                    product = featuredProduct,
//                    onProductClick = onProductClick,
//                    modifier = Modifier
//                        .height(dimens.featuredCardHeight)
//
//                )
               ProductCard(
                   product = featuredProduct,
                   onClick = onProductClick,
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(dimens.featuredCardHeight),
                   cornerRadius = dimens.radiusMedium
               )
                ProductRow(
                    product = featuredProduct,
                    onAddToCart = onAddToCart
                )
                Spacer(modifier = Modifier.height(dimens.spaceMd))
                Text(
                    text = "Sản phẩm gợi ý",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(dimens.spaceSm))
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm)
                ){
                    items(productRecommends){ product ->
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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val fakeProducts = listOf(
        Product(1, "Giày sneakers", "Description 1", 40.55, "", R.drawable.pic_shoe_1),
        Product(2, "Product 2", "Description 2", 20.0, "", R.drawable.pic_shoe_1),
        Product(3, "Product 3", "Description 3", 30.0, "", R.drawable.pic_shoe_1)
    )

    EasyMartTheme{
        Surface {
            HomeScreen(productRecommends = fakeProducts, onAddToCart = {}, onProductClick = {} )
        }
    }
}