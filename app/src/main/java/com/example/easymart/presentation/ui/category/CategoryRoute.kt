package com.example.easymart.presentation.ui.category

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.easymart.domain.model.Product
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CategoryRoute(
    viewModel: CategoryViewModel = hiltViewModel(),
    onNavigateToProduct: (Product) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is CategoryUiEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
                is CategoryUiEvent.NavigateToProduct -> onNavigateToProduct(event.product)
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        val padding = innerPadding
        CategoryScreen(
            uiState = uiState.value,
            onCategorySelected = viewModel::onCategorySelected,
            onSortSelected = viewModel::onSortSelected,
            onStockFilterSelected = viewModel::onStockFilterSelected,
            onPriceFilterSelected = viewModel::onPriceFilterSelected,
            onRatingFilterSelected = viewModel::onRatingFilterSelected,
            onProductClick = viewModel::onProductClick,
            onAddToCartClick = viewModel::addProductToCart,
        )
    }
}
