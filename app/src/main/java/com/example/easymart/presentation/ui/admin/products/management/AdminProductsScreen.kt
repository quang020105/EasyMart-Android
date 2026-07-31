package com.example.easymart.presentation.ui.admin.products.management
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.admin.products.management.components.AdminProductCard
import com.example.easymart.presentation.ui.admin.products.management.components.EmptyState
import com.example.easymart.presentation.ui.admin.products.management.components.ErrorBanner
import com.example.easymart.presentation.ui.admin.products.management.components.FilterSection
import com.example.easymart.presentation.ui.admin.products.management.components.HeaderSection
import com.example.easymart.presentation.ui.admin.products.management.components.SummaryRow
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
    onToggleVisibility: (Product, Boolean) -> Unit,
    onViewProductDetail: (Int) -> Unit
) {
    val dimens = LocalAppDimens.current

    val filteredProducts = filterProducts(uiState)
    val totalCount = uiState.products.size
    val visibleCount = uiState.products.count { it.isVisible }
    val hiddenCount = uiState.products.count { !it.isVisible }
    val outOfStockCount = uiState.products.count { it.stockQuantity <= 0 }
    var isFilterVisible by rememberSaveable {
        mutableStateOf(true)
    }

    // để xác định xem có bộ lọc nào đang được áp dụng hay không, dùng để hiển thị trạng thái của nút lọc
    val hasActiveFilter =
        uiState.selectedCategory != null ||
                uiState.onlyLowStock ||
                uiState.sourceFilter != ProductSourceFilter.ALL

    val apiCount = uiState.products.count {
        it.storagePath.isNullOrBlank() && it.localImageUri.isNullOrBlank()
    }
    val addedCount = totalCount - apiCount

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                contentPadding = PaddingValues(
                    start = dimens.screenPadding,
                    end = dimens.screenPadding,
                    top = 18.dp,
                    bottom = 32.dp
                )
            ) {
                item {
                    HeaderSection(
                        query = uiState.searchQuery,
                        onQueryChange = onSearchQueryChange,
                        isFilterVisible = isFilterVisible,
                        hasActiveFilter = hasActiveFilter,
                        onToggleFilterClick = { isFilterVisible = !isFilterVisible }
                    )
                }

                item {
                    AnimatedVisibility(
                        visible = isFilterVisible,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        FilterSection(
                            totalCount = totalCount,
                            apiCount = apiCount,
                            addedCount = addedCount,
                            hiddenCount = hiddenCount,
                            outOfStockCount = outOfStockCount,
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
                }

                item {
                    SummaryRow(
                        total = totalCount,
                        visibleCount = visibleCount
                    )
                }

                if (!uiState.isLoading && uiState.error == null && filteredProducts.isNotEmpty()) {
                    items(
                        items = filteredProducts,
                        key = { product -> product.id },
                    ) { product ->
                        AdminProductCard(
                            product = product,
                            onEdit = { onEditProduct(product.id) },
                            onImport = {},
                            onToggleVisibility = { checked ->
                                onToggleVisibility(product, checked)
                            },
                            onViewDetail = { onViewProductDetail(product.id) }
                        )
                    }
                }

                if (!uiState.isLoading && uiState.error == null && filteredProducts.isEmpty()) {
                    item {
                        EmptyState(onRefresh = onRefresh)
                    }

                    
                }

                if (uiState.error != null) {
                    item {
                        ErrorBanner(
                            message = uiState.error,
                            onRefresh = onRefresh
                        )
                    }
                }
            }

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

fun filterProducts(uiState: AdminProductsUiState): List<Product> {
    val sourceFiltered = uiState.products
        .filter { product ->
            uiState.selectedCategory == null || product.category == uiState.selectedCategory
        }
        .filter { product ->
            !uiState.onlyLowStock || product.stockQuantity in 0..uiState.lowStockThreshold
        }
        .filter { product ->
            uiState.searchQuery.isBlank() ||
                    product.name.contains(uiState.searchQuery, ignoreCase = true)
        }
        .filter { product ->
            when (uiState.sourceFilter) {
                ProductSourceFilter.ALL -> true
                ProductSourceFilter.API ->
                    product.storagePath.isNullOrBlank() && product.localImageUri.isNullOrBlank()

                ProductSourceFilter.ADDED ->
                    !product.storagePath.isNullOrBlank() || !product.localImageUri.isNullOrBlank()
            }
        }

    return when (uiState.sortType) {
        AdminProductSort.UPDATED_DESC -> sourceFiltered.sortedByDescending { it.updatedAt }
        AdminProductSort.NAME_ASC -> sourceFiltered.sortedBy { it.name.lowercase() }
        AdminProductSort.PRICE_ASC -> sourceFiltered.sortedBy { it.priceVnd }
    }
}

fun formatPrice(price: Long): String {
    return "${"%,d".format(price.toLong())} đ"
}

@Preview(showBackground = true)
@Composable
fun AdminProductsScreenPreview() {
    EasyMartTheme {
        AdminProductsScreen(
            uiState = AdminProductsUiState(
                isLoading = false,
                products = mockProducts,
                categories = listOf(stringResource(R.string.ui_text_163), stringResource(R.string.ui_text_164), stringResource(R.string.ui_text_165), stringResource(R.string.ui_text_166)),
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
            onToggleVisibility = { _, _ -> },
            onViewProductDetail = {}
        )
    }
}
