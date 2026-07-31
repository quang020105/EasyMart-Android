package com.example.easymart.presentation.ui.orderdetail
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.easymart.presentation.ui.common.components.OrderActionConfirmationDialog

@Composable
fun OrderDetailRoute(
    viewModel: OrderDetailViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val cancellationState by viewModel.cancellationState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                is OrderDetailUiEvent.NavigateToBuyAgain -> Unit
                is OrderDetailUiEvent.NavigateToTrack -> Unit
                is OrderDetailUiEvent.NavigateToReview -> Unit
                is OrderDetailUiEvent.ShowToast -> snackbarHostState.showSnackbar(event.message)
                is OrderDetailUiEvent.ShowConfirmCancel -> Unit
            }
        }
    }

    if (cancellationState.pendingOrderId != null) {
        val isDirectCancellation = cancellationState.mode == CustomerCancellationMode.DIRECT
        OrderActionConfirmationDialog(
            title = if (isDirectCancellation) stringResource(R.string.ui_text_247) else stringResource(R.string.ui_text_311),
            message = if (isDirectCancellation) {
                "Đơn hàng sẽ được hủy ngay. Nếu đơn đã thanh toán, hệ thống sẽ chuyển đơn sang chờ hoàn tiền."
            } else {
                "Yêu cầu hủy của bạn sẽ được gửi đến quản trị viên để kiểm tra và xử lý."
            },
            confirmText = if (isDirectCancellation) stringResource(R.string.ui_text_249) else stringResource(R.string.ui_text_312),
            icon = if (isDirectCancellation) Icons.Rounded.Cancel else Icons.Rounded.Info,
            isDanger = isDirectCancellation,
            isLoading = cancellationState.isSubmitting,
            onDismiss = viewModel::dismissCancellation,
            onConfirm = viewModel::confirmCancellation,
            content = {
                OutlinedTextField(
                    value = cancellationState.reason,
                    onValueChange = viewModel::updateCancellationReason,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.ui_text_313)) },
                    placeholder = { Text(stringResource(R.string.ui_text_314)) },
                    enabled = !cancellationState.isSubmitting,
                    minLines = 3,
                    maxLines = 4
                )
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        OrderDetailScreen(
            uiState = uiState,
            onRequestCancellation = viewModel::requestCancellation,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}
