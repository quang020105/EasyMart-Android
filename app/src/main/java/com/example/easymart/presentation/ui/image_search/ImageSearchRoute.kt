package com.example.easymart.presentation.ui.image_search

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.easymart.domain.model.Product

@Composable
fun ImageSearchRoute(
    viewModel: ImageSearchViewModel = hiltViewModel(),
    onProductClick: (Product) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        val message = uiState.error ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(message)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        val padding = innerPadding
        ImageSearchScreen(
            uiState = uiState,
            onImageSelected = viewModel::onImageSelected,
            onSearchClick = viewModel::search,
            onRetryClick = viewModel::retry,
            onClearClick = viewModel::clear,
            onProductClick = onProductClick
        )
    }
}