package com.example.easymart.presentation.ui.admin.products.detail
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun AdminProductDetailRoute(
    productId: Int,
    onEditProduct: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: AdminProductDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    val product = uiState.product

    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        product != null -> {
            AdminProductDetailScreen(
                product = product,
                onEditProduct = onEditProduct,
                onToggleVisibility = { _, visible -> viewModel.toggleVisibility(visible) }
            )
        }
        else -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = uiState.error ?: stringResource(R.string.ui_text_138))
            }
        }
    }
}
