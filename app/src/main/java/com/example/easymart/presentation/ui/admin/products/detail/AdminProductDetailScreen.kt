package com.example.easymart.presentation.ui.admin.products.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.ui.admin.products.detail.components.ProductDescriptionCard
import com.example.easymart.presentation.ui.admin.products.detail.components.ProductDetailBottomActions
import com.example.easymart.presentation.ui.admin.products.detail.components.ProductDetailInfoCard
import com.example.easymart.presentation.ui.admin.products.detail.components.ProductOverviewCard
import com.example.easymart.presentation.ui.admin.products.detail.components.ProductStatisticCard
import com.example.easymart.presentation.ui.mock.mockSimpleProduct

@Composable
fun AdminProductDetailScreen(
    product: Product,
    onEditProduct: (Int) -> Unit,
    onToggleVisibility: (Product, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ProductOverviewCard(product = product)
            }

            item {
                ProductDetailInfoCard(product = product)
            }

            item {
                ProductDescriptionCard(
                    description = product.description.orEmpty()
                )
            }

            item {
                ProductStatisticCard(product = product)
            }

            item {
                ProductDetailBottomActions(
                    isVisible = product.isVisible,
                    onToggleVisibilityClick = {
                        onToggleVisibility(product, !product.isVisible)
                    },
                    onEditClick = {
                        onEditProduct(product.id)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun AdminProductDetailScreenPreview() {
    EasyMartTheme {
        AdminProductDetailScreen(
            product = mockSimpleProduct,
            onEditProduct = {},
            onToggleVisibility = { _, _ -> }
        )
    }
}