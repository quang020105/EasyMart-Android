package com.example.easymart.presentation.ui.admin.orders.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

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
                is AdminOrderDetailUiEvent.ShowMessage -> {
                    snackbarHostState.showSnackbar(event.message)
                }
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
        val order = uiState.order
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

            order != null -> {
                AdminOrderDetailScreen(
                    order = order,
                    isActionLoading = uiState.isActionLoading,
                    onConfirmOrder = viewModel::confirmOrder,
                    onCancelOrder = viewModel::cancelOrder,
                    onMoveToProcessing = viewModel::moveToProcessing,
                    onMoveToShipping = viewModel::moveToShipping,
                    onConfirmDelivered = viewModel::confirmDelivered,
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

// hộp thoại xác nhận hành động
@Composable
private fun AdminOrderConfirmationDialog(
    confirmation: AdminOrderConfirmation,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            if (!isLoading) onDismiss()
        },
        title = {
            Text(text = confirmation.title)
        },
        text = {
            Text(text = confirmation.message)
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isLoading
            ) {
                Text(text = confirmation.confirmText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text(
                    text = "Đóng",
                    color = if (confirmation.isDanger) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            }
        }
    )
}
