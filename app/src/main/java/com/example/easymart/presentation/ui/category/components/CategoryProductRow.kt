package com.example.easymart.presentation.ui.category.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.mock.mockProducts

@Composable
fun CategoryProductRow(
    products: List<Product>,
    onProductClick: (Product) -> Unit,
    onAddToCartClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = dimens.spaceSm),
        horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm)
    ) {
        products.forEach { product ->
            CategoryProductCard(
                product = product,
                onProductClick = onProductClick,
                onAddToCartClick = onAddToCartClick,
                modifier = Modifier.weight(1f)
            )
        }
        if (products.size == 1) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
fun CategoryProductRowPreview() {
    CategoryProductRow(
        products = mockProducts,
        onProductClick = {},
        onAddToCartClick = {}
    )
}

