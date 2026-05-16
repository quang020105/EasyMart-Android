package com.example.easymart.presentation.ui.admin.products.management

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun AdminProductsRoute(
    viewModel: AdminProductsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onAddProduct: () -> Unit,
    onEditProduct: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    AdminProductsScreen(
        uiState = uiState,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onSelectCategory = viewModel::onSelectCategory,
        onToggleLowStock = viewModel::onToggleLowStock,
        onSelectSource = viewModel::onSelectSource,
        onSelectSort = viewModel::onSelectSort,
        onRefresh = viewModel::refresh,
        onNavigateBack = onNavigateBack,
        onAddProduct = onAddProduct,
        onEditProduct = onEditProduct,
        onToggleVisibility = viewModel::onToggleVisibility
    )
}
