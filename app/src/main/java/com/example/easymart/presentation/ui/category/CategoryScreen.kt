package com.example.easymart.presentation.ui.category
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.easymart.presentation.ui.category.components.CategoryEmptyState
import com.example.easymart.presentation.ui.category.components.CategoryErrorBanner
import com.example.easymart.presentation.ui.category.components.CategorySelectorRow
import com.example.easymart.presentation.ui.category.components.CategorySortFilterBar
import com.example.easymart.presentation.ui.category.components.CategoryProductRow
import com.example.easymart.presentation.ui.category.components.CustomerProductPriceFilter
import com.example.easymart.presentation.ui.category.components.CustomerProductRatingFilter
import com.example.easymart.presentation.ui.category.components.CustomerProductSort
import com.example.easymart.presentation.ui.category.components.CustomerProductStockFilter
import com.example.easymart.presentation.ui.mock.mockProducts

@Composable
fun CategoryScreen(
    uiState: CategoryUiState,
    onCategorySelected: (String?) -> Unit,
    onSortSelected: (CustomerProductSort) -> Unit,
    onStockFilterSelected: (CustomerProductStockFilter) -> Unit,
    onPriceFilterSelected: (CustomerProductPriceFilter) -> Unit,
    onRatingFilterSelected: (CustomerProductRatingFilter) -> Unit,
    onProductClick: (Product) -> Unit,
    onAddToCartClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (uiState.isLoading && uiState.allProducts.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            return@Box
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(dimens.screenPadding)
        ) {
            item {
                Text(
                    text = stringResource(R.string.ui_text_216),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            item {
                Spacer(modifier = Modifier.height(dimens.spaceMd))
                CategorySelectorRow(
                    categories = uiState.categories,
                    selectedCategory = uiState.selectedCategory,
                    onCategorySelected = onCategorySelected,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(modifier = Modifier.height(dimens.spaceSm))
                CategorySortFilterBar(
                    selectedSort = uiState.sort,
                    selectedStockFilter = uiState.stockFilter,
                    selectedPriceFilter = uiState.priceFilter,
                    selectedRatingFilter = uiState.ratingFilter,
                    onSortSelected = onSortSelected,
                    onStockFilterSelected = onStockFilterSelected,
                    onPriceFilterSelected = onPriceFilterSelected,
                    onRatingFilterSelected = onRatingFilterSelected,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            uiState.error?.let { message ->
                item {
                    Spacer(modifier = Modifier.height(dimens.spaceMd))
                    CategoryErrorBanner(message = message)
                }
            }

            item {
                Spacer(modifier = Modifier.height(dimens.spaceMd))
                Text(
                    text = if (uiState.searchQuery.isBlank()) {
                        "${uiState.products.size} sản phẩm"
                    } else {
                        "${uiState.products.size} kết quả cho \"${uiState.searchQuery}\""
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(dimens.spaceSm))
            }

            if (uiState.products.isEmpty()) {
                item {
                    CategoryEmptyState()
                }
            } else {
                items(uiState.products.chunked(2)) { rowProducts ->
                    CategoryProductRow(
                        products = rowProducts,
                        onProductClick = onProductClick,
                        onAddToCartClick = onAddToCartClick
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryScreenPreview() {
    EasyMartTheme {
        CategoryScreen(
            uiState = CategoryUiState(
                allProducts = mockProducts,
                products = mockProducts,
                categories = mockProducts.map(Product::category).filter(String::isNotBlank).distinct()
            ),
            onCategorySelected = {},
            onSortSelected = {},
            onStockFilterSelected = {},
            onPriceFilterSelected = {},
            onRatingFilterSelected = {},
            onProductClick = {},
            onAddToCartClick = {}
        )
    }
}
