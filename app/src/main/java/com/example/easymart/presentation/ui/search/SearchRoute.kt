package com.example.easymart.presentation.ui.search

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.easymart.domain.model.Product
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchRoute(
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToProduct: (Product) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is SearchUiEvent.ShowMessage -> {
                    snackBarHostState.showSnackbar(event.message)
                }
                is SearchUiEvent.NavigateToProduct -> {
                    onNavigateToProduct(event.product)
                }
            }
        }
    }

    SearchScreen(
        uiState = uiState.value,
//        onSearchTextChange = { query -> viewModel.onSearchTextChange(query) },
//        onSearchClick = { query -> viewModel.searchProducts(query) },
//        onBackClick = onNavigateBack,
        onProductClick = { product -> viewModel.onProductClick(product) },
        onSuggestionClick = { suggestion -> viewModel.onSuggestionClick(suggestion) }
    )
}

