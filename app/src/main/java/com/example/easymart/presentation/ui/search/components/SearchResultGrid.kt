package com.example.easymart.presentation.ui.search.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.common.components.ItemProductRecommendCard
import com.example.easymart.presentation.ui.mock.mockProducts

@Composable
fun SearchResultGrid(
    products: List<Product>,
    onProductClick: (Product) -> Unit
) {
    val dimens = LocalAppDimens.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.spaceMd)
            .background(MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm),
        verticalArrangement = Arrangement.spacedBy(dimens.spaceSm),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = dimens.spaceSm)
    ) {
        items(products) { product ->
            ItemProductRecommendCard(
                product = product,
                onProductClick = onProductClick
            )
        }
    }
}

@Preview
@Composable
fun SearchResultGridPreview() {
    EasyMartTheme {
        SearchResultGrid(
            products = mockProducts,
            onProductClick = {}
        )
    }
}
