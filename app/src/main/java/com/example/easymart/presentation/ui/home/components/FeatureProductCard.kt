package com.example.easymart.presentation.ui.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.R
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun FeatureProductCard(
    product: Product,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    Card (
        modifier = modifier.fillMaxWidth()
            .height(dimens.featuredCardHeight),
        shape = RoundedCornerShape(dimens.radiusMedium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant),

        onClick = { onProductClick(product) }

    ){
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center){
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun FeatureProductCardPreview() {
    EasyMartTheme {
        Surface {
            FeatureProductCard(
                product = Product(
                    id = 1,
                    name = "Sample Product",
                    imageUrl = "",
                    priceVnd = 249_000L,
                    description = "This is a sample product description.",
                    imageRes = R.drawable.pic_shoe_1
                ),
                onProductClick = {},
            )
        }
    }
}
