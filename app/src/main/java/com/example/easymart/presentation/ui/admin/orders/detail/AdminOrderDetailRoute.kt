package com.example.easymart.presentation.ui.admin.orders.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.easymart.presentation.ui.common.components.OrderActionConfirmationDialog

@Composable
fun AdminOrderDetailRoute(
    remoteId: String,
    viewModel: AdminOrderDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(remoteId) {
        viewModel.loadOrder(remoteId)
    }

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AdminOrderDetailUiEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    uiState.pendingConfirmation?.let { confirmation ->
        AdminOrderConfirmationDialog(
            confirmation = confirmation,
            isLoading = uiState.isActionLoading,
            onDismiss = viewModel::dismissConfirmation,
            onConfirm = viewModel::confirmPendingAction
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.order != null -> {
                AdminOrderDetailScreen(
                    order = requireNotNull(uiState.order),
                    isActionLoading = uiState.isActionLoading,
                    onConfirmOrder = viewModel::confirmOrder,
                    onCancelOrder = viewModel::cancelOrder,
                    onMoveToProcessing = viewModel::moveToProcessing,
                    onMoveToShipping = viewModel::moveToShipping,
                    onConfirmDelivered = viewModel::confirmDelivered,
                    onConfirmRefund = viewModel::confirmRefund,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = uiState.errorMessage ?: "Không tìm thấy đơn hàng")
                }
            }
        }
    }
}

@Composable
private fun AdminOrderConfirmationDialog(
    confirmation: AdminOrderConfirmation,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    OrderActionConfirmationDialog(
        title = confirmation.title,
        message = confirmation.message,
        confirmText = confirmation.confirmText,
        icon = if (confirmation.isDanger) Icons.Rounded.Cancel else Icons.Rounded.CheckCircle,
        isDanger = confirmation.isDanger,
        isLoading = isLoading,
        onDismiss = onDismiss,
        onConfirm = onConfirm
    )
}
