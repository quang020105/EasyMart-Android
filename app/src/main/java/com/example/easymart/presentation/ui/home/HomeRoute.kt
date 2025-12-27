package com.example.easymart.presentation.ui.home

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.ui.cart.CartEvent
import com.example.easymart.presentation.ui.cart.CartViewModel
import com.example.easymart.presentation.ui.mock.mockSimpleProduct
import com.example.easymart.presentation.ui.productdetail.ProductDetailScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    onNavigateToProduct: (Product) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState = homeViewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        //event khi nhấn xem chi tiết và tải dữ liệu product từ remote
        homeViewModel.event.collectLatest { event ->
            when (event) {
                is HomeUiEvent.ShowMessage -> {
                    snackBarHostState.showSnackbar(event.message)
                }

                is HomeUiEvent.NavigateToProduct -> {
                    onNavigateToProduct(event.product)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        //event khi thêm sp vào giỏ hàng
        cartViewModel.event.collectLatest { event ->
            when (event) {
                is CartEvent.ShowMessage -> {
                    snackBarHostState.showSnackbar(event.message)
                    Log.d("HomeRoute", "ShowMessage: ${event.message}")
                }
            }
        }
    }


    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { innerPadding ->
        HomeScreen(
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
            productRecommends = uiState.value.products,
            onAddToCart = { product -> cartViewModel.addProductToCart(product) },
            onProductClick = { product -> homeViewModel.onProductClick(product) }
        )
    }
}