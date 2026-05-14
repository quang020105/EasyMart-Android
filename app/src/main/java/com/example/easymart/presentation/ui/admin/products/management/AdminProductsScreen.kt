package com.example.easymart.presentation.ui.admin.products.management

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.admin.products.components.HeaderSection
import com.example.easymart.presentation.ui.admin.products.management.AdminProductsUiState
import com.example.easymart.presentation.ui.admin.products.components.AdminProductCard
import com.example.easymart.presentation.ui.admin.products.components.EmptyState
import com.example.easymart.presentation.ui.admin.products.components.ErrorBanner
import com.example.easymart.presentation.ui.admin.products.components.FilterSection
import com.example.easymart.presentation.ui.admin.products.components.HeaderSection
import com.example.easymart.presentation.ui.admin.products.components.SummaryRow
import com.example.easymart.presentation.ui.mock.mockProducts

@Composable
fun AdminProductsScreen(
    uiState: AdminProductsUiState,
    onSearchQueryChange: (String) -> Unit,
    onSelectCategory: (String?) -> Unit,
    onToggleLowStock: (Boolean) -> Unit,
    onRefresh: () -> Unit,
    onNavigateBack: () -> Unit,
    onAddProduct: () -> Unit,
    onEditProduct: (Int) -> Unit
) {
    val dimens = LocalAppDimens.current

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(modifier = Modifier.fillMaxSize()) {
            val filtered = filterProducts(uiState)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(dimens.spaceSm),
                contentPadding = PaddingValues(
                    start = dimens.screenPadding,
                    end = dimens.screenPadding,
                    bottom = dimens.spaceLg
                )
            ) {
                item {
                    HeaderSection(
                        query = uiState.searchQuery,
                        onQueryChange = onSearchQueryChange,
                        onNavigateBack = onNavigateBack,
                        onAddProduct = onAddProduct
                    )
                }

                item {
                    FilterSection(
                        categories = uiState.categories,
                        selectedCategory = uiState.selectedCategory,
                        onlyLowStock = uiState.onlyLowStock,
                        onSelectCategory = onSelectCategory,
                        onToggleLowStock = onToggleLowStock
                    )
                }

                item {
                    SummaryRow(
                        total = filtered.size,
                        lowStockCount = filtered.count { it.stockQuantity in 0..uiState.lowStockThreshold }
                    )
                }

                items(filtered, key = { it.id }) { product ->
                    AdminProductCard(product = product, onEdit = { onEditProduct(product.id) })
                }
            }

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            if (!uiState.isLoading && filtered.isEmpty()) {
                EmptyState(onRefresh = onRefresh)
            }

            if (uiState.error != null) {
                ErrorBanner(message = uiState.error, onRefresh = onRefresh)
            }
        }
    }
}



fun filterProducts(uiState: AdminProductsUiState): List<Product> {
    return uiState.products
        .filter { product ->
            if (uiState.selectedCategory == null) true else product.category == uiState.selectedCategory
        }
        .filter { product ->
            if (uiState.onlyLowStock) product.stockQuantity in 0..uiState.lowStockThreshold else true
        }
        .filter { product ->
            if (uiState.searchQuery.isBlank()) true else product.name.contains(uiState.searchQuery, ignoreCase = true)
        }
}

fun formatPrice(price: Double): String {
    return "${"%,d".format(price.toLong())} đ"
}

@Preview
@Composable
fun AdminProductsScreenPreview() {
    EasyMartTheme {
        AdminProductsScreen(
            uiState = AdminProductsUiState(
                isLoading = false,
                products = mockProducts,
                categories = listOf("Điện thoại", "Laptop", "Phụ kiện"),
                searchQuery = "",
                selectedCategory = null,
                onlyLowStock = false,
                lowStockThreshold = 5,
                error = null
            ),
            onSearchQueryChange = {},
            onSelectCategory = {},
            onToggleLowStock = {},
            onRefresh = {},
            onNavigateBack = {},
            onAddProduct = {},
            onEditProduct = {}
        )
    }
}

@Preview
@Composable
fun AdminProductsScreenPreview2() {
    MaterialTheme {
        AdminProductsScreen(
            uiState = AdminProductsUiState(
                isLoading = false,
                products = mockProducts,
                categories = listOf("Điện thoại", "Laptop", "Phụ kiện"),
                searchQuery = "",
                selectedCategory = null,
                onlyLowStock = false,
                lowStockThreshold = 5,
                error = null
            ),
            onSearchQueryChange = {},
            onSelectCategory = {},
            onToggleLowStock = {},
            onRefresh = {},
            onNavigateBack = {},
            onAddProduct = {},
            onEditProduct = {}
        )
    }
}
