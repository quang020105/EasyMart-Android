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
import com.example.easymart.presentation.ui.admin.products.components.AdminProductCard
import com.example.easymart.presentation.ui.admin.products.components.EmptyState
import com.example.easymart.presentation.ui.admin.products.components.ErrorBanner
import com.example.easymart.presentation.ui.admin.products.components.FilterSection
import com.example.easymart.presentation.ui.admin.products.components.SummaryRow
import com.example.easymart.presentation.ui.mock.mockProducts

@Composable
fun AdminProductsScreen(
    uiState: AdminProductsUiState,
    onSearchQueryChange: (String) -> Unit,
    onSelectCategory: (String?) -> Unit,
    onToggleLowStock: (Boolean) -> Unit,
    onSelectSource: (ProductSourceFilter) -> Unit,
    onSelectSort: (AdminProductSort) -> Unit,
    onRefresh: () -> Unit,
    onNavigateBack: () -> Unit,
    onAddProduct: () -> Unit,
    onEditProduct: (Int) -> Unit,
    onToggleVisibility: (Product, Boolean) -> Unit
) {
    val dimens = LocalAppDimens.current

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(modifier = Modifier.fillMaxSize()) {
            val filtered = filterProducts(uiState)
            val totalCount = uiState.products.size
            val visibleCount = uiState.products.count { it.isVisible }
            val apiCount =
                uiState.products.count { it.storagePath.isNullOrBlank() && it.localImageUri.isNullOrBlank() }
            val addedCount = totalCount - apiCount

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(dimens.spaceSm),
                contentPadding = PaddingValues(
                    start = dimens.screenPadding,
                    end = dimens.screenPadding,
                    top = dimens.spaceSm,
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
                        totalCount = totalCount,
                        apiCount = apiCount,
                        addedCount = addedCount,
                        categories = uiState.categories,
                        selectedCategory = uiState.selectedCategory,
                        onlyLowStock = uiState.onlyLowStock,
                        sourceFilter = uiState.sourceFilter,
                        sortType = uiState.sortType,
                        onSelectCategory = onSelectCategory,
                        onToggleLowStock = onToggleLowStock,
                        onSelectSource = onSelectSource,
                        onSelectSort = onSelectSort
                    )
                }

                item {
                    SummaryRow(
                        total = totalCount,
                        visibleCount = visibleCount
                    )
                }

                items(filtered, key = { it.id }) { product ->
                    AdminProductCard(
                        product = product,
                        onEdit = { onEditProduct(product.id) },
                        onImport = {},
                        onToggleVisibility = { checked -> onToggleVisibility(product, checked) }
                    )
                }

                item {
                    if (uiState.isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
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
    }
}


fun filterProducts(uiState: AdminProductsUiState): List<Product> {
    val sourceFiltered = uiState.products
        .filter { product ->
            if (uiState.selectedCategory == null) true else product.category == uiState.selectedCategory
        }
        .filter { product ->
            if (uiState.onlyLowStock) product.stockQuantity in 0..uiState.lowStockThreshold else true
        }
        .filter { product ->
            if (uiState.searchQuery.isBlank()) true else product.name.contains(
                uiState.searchQuery,
                ignoreCase = true
            )
        }
        .filter { product ->
            when (uiState.sourceFilter) {
                ProductSourceFilter.ALL -> true
                ProductSourceFilter.API -> product.storagePath.isNullOrBlank() && product.localImageUri.isNullOrBlank()
                ProductSourceFilter.ADDED -> product.storagePath?.isNotBlank() == true
            }
        }

    return when (uiState.sortType) {
        AdminProductSort.UPDATED_DESC -> sourceFiltered.sortedByDescending { it.updatedAt }
        AdminProductSort.NAME_ASC -> sourceFiltered.sortedBy { it.name.lowercase() }
        AdminProductSort.PRICE_ASC -> sourceFiltered.sortedBy { it.price }
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
            onSelectSource = {},
            onSelectSort = {},
            onRefresh = {},
            onNavigateBack = {},
            onAddProduct = {},
            onEditProduct = {},
            onToggleVisibility = { _, _ -> }
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
            onSelectSource = {},
            onSelectSort = {},
            onRefresh = {},
            onNavigateBack = {},
            onAddProduct = {},
            onEditProduct = {},
            onToggleVisibility = { _, _ -> }
        )
    }
}
