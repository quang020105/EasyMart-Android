package com.example.easymart.presentation.ui.productdetail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
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
import com.example.easymart.presentation.ui.mock.mockProducts

@Composable
internal fun ProductHeroCard(
    product: Product,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current
    val isPreview = LocalInspectionMode.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(dimens.productImageHeight),
        shape = RoundedCornerShape(dimens.radiusXl),
        colors = CardDefaults.cardColors(containerColor = appColors.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.cardElevation),
        border = androidx.compose.foundation.BorderStroke(
            width = dimens.dividerThickness,
            color = appColors.outlineVariant
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(appColors.surfaceContainerHigh)
                .padding(dimens.spaceLg),
            contentAlignment = Alignment.Center
        ) {
            if (isPreview && product.imageRes != 0) {
                Image(
                    painter = painterResource(product.imageRes),
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
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
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(R.drawable.pic_shoe_1),
                    error = painterResource(R.drawable.pic_shoe_1)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductHeroCardPreview() {
    EasyMartTheme {
        ProductHeroCard(product = mockProducts.first())
    }
}
