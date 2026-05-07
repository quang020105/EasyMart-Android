package com.example.easymart.presentation.ui.admin.products

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
        onRefresh = viewModel::refresh,
        onNavigateBack = onNavigateBack,
        onAddProduct = onAddProduct,
        onEditProduct = onEditProduct
    )
}

