package com.example.easymart.presentation.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.common.components.ProductCard

@Composable
fun ItemProductRecommendCard(
    product: Product,
    onProductClick: (Product) -> Unit
) {
    val dimens = LocalAppDimens.current
    Column (
        modifier = Modifier.width(dimens.recommendedWidth)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        ProductCard(
            product = product,
            onClick = onProductClick,
            modifier = Modifier
                .width(dimens.recommendedWidth)
                .height(dimens.recommendedHeight),
            cornerRadius = dimens.radiusLarge
        )
        Spacer(modifier = Modifier.height(dimens.spaceSm))
        Text(
            text = product.name,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(dimens.spaceXs))
        Text(
            text = "$${product.price}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

    }
}

@Preview(showBackground = false)
@Composable
fun ItemProductRecommendCardPreview() {
    EasyMartTheme {
        ItemProductRecommendCard(
            product = Product(
                id = 1,
                name = "Giày sneaker",
                price = 9.99,
                imageRes = com.example.easymart.R.drawable.pic_shoe_1,
                description = "This is a sample product description.",
                imageUrl = ""
            ),
            onProductClick = {}
        )
    }
}