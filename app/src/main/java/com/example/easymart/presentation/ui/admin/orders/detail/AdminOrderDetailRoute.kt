package com.example.easymart.presentation.ui.admin.orders.detail

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
fun AdminOrderDetailRoute(
    remoteId: String,
    viewModel: AdminOrderDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(remoteId) {
        viewModel.loadOrder(remoteId)
    }

    val order = uiState.order
    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        order != null -> {
            AdminOrderDetailScreen(
                order = order,
                isActionLoading = uiState.isActionLoading,
                onConfirmOrder = viewModel::confirmOrder,
                onCancelOrder = viewModel::cancelOrder,
                onMoveToProcessing = viewModel::moveToProcessing,
                onMoveToShipping = viewModel::moveToShipping,
                onConfirmDelivered = viewModel::confirmDelivered
            )
        }
        else -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = uiState.errorMessage ?: "Không tìm thấy đơn hàng")
            }
        }
    }
}
