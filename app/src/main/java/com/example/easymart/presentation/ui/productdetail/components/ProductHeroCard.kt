package com.example.easymart.presentation.ui.productdetail.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.mock.mockProducts

@Composable
internal fun ProductHeroCard(
    product: Product,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current

    ProductImageGallery(
        product = product,
        modifier = modifier
            .fillMaxWidth()
            .height(dimens.productImageHeight)
    )
}

@Preview(showBackground = true)
@Composable
private fun ProductHeroCardPreview() {
    EasyMartTheme {
        ProductHeroCard(product = mockProducts.first())
    }
}