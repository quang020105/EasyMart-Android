package com.example.easymart.presentation.ui.productdetail

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.easymart.R
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.navigation.Screen
import com.example.easymart.presentation.ui.mock.mockProducts
import com.example.easymart.presentation.ui.mock.mockSimpleProduct
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProductDetailRoute(
    viewModel: ProductDetailViewModel,
    onNavigateToProduct: (Product) -> Unit,
    onNavigateToQuickCheckout: (Product, Int) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    Log.d("Detail", "uiState: ${uiState.value}")

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is ProductDetailUiEvent.ShowMessage -> {
                    snackBarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    Scaffold (
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ){ innerPadding ->
        val padding = innerPadding
        ProductDetailScreen(
            uiState = uiState.value,
            onAddToCartClick = { product, quantity -> viewModel.addProductToCart(product, quantity) },
            onRecommendedProductClick = onNavigateToProduct,
            onBuyNowClick = onNavigateToQuickCheckout
        )
    }
}