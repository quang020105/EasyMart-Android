package com.example.easymart.presentation.ui.common.components

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import com.example.easymart.R
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun ProductCard(
    product: Product,
    modifier: Modifier = Modifier,
    onClick: (Product) -> Unit = {},
    cornerRadius: Dp = 12.dp,
    colorBackground: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    //lấy chế độ đang là preview hay không
    val isPreview = LocalInspectionMode.current
    Card(
        modifier = modifier.fillMaxSize(),
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = colorBackground
        ),
        onClick = { onClick(product) }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if(isPreview && product.imageRes != 0){
                Image(
                    painter = painterResource(id = product.imageRes),
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxWidth(),
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
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Fit,
                    error = painterResource(id = R.drawable.pic_shoe_1)
                )
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun ProductCardPreview() {
    val dimens = LocalAppDimens.current
    EasyMartTheme {
        ProductCard(
            product = Product(
                id = 1,
                name = "Sample Product",
                priceVnd = 249_000L,
                imageRes = com.example.easymart.R.drawable.pic_shoe_1,
                description = "This is a sample product description.",
                imageUrl = ""
            ),
            onClick = {},
            cornerRadius = dimens.radiusMedium,
            modifier = Modifier
                .width(dimens.recommendedWidth)
                .height(dimens.recommendedHeight)
        )
    }
}
