package com.example.easymart.presentation.ui.admin.orders.management

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun AdminOrdersRoute(
    onViewOrderDetail: (String) -> Unit,
    viewModel: AdminOrdersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading && uiState.orders.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        uiState.errorMessage != null && uiState.orders.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = uiState.errorMessage.orEmpty())
            }
        }
        else -> {
            AdminOrdersScreen(
                orders = uiState.orders,
                searchQuery = uiState.searchQuery,
                selectedOrderStatus = uiState.selectedOrderStatus,
                selectedPaymentMethod = uiState.selectedPaymentMethod,
                selectedPaymentStatus = uiState.selectedPaymentStatus,
                onSearchQueryChange = viewModel::onSearchQueryChange,
                onOrderStatusSelected = viewModel::onOrderStatusSelected,
                onPaymentMethodSelected = viewModel::onPaymentMethodSelected,
                onPaymentStatusSelected = viewModel::onPaymentStatusSelected,
                onOrderClick = { order -> onViewOrderDetail(order.id) }
            )
        }
    }
}
