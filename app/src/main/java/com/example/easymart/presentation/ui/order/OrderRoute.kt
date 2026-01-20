package com.example.easymart.presentation.ui.order

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun OrderRoute(
    viewModel: OrderViewModel,
    onNavigateToOrderDetail: (orderId: Int) -> Unit = {},
    onNavigateToTrack: (orderId: Int) -> Unit = {},
    onNavigateToBuyAgain: (orderId: Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    Log.d("OrderRoute", "uiState: $uiState")

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is OrderUiEvent.NavigateToOrderDetail -> {
                    onNavigateToOrderDetail(event.orderId)
                }

                is OrderUiEvent.NavigateToTrack -> {
                    onNavigateToTrack(event.orderId)
                }

                is OrderUiEvent.NavigateToBuyAgain -> {
                    onNavigateToBuyAgain(event.orderId)
                }

                is OrderUiEvent.ShowMessage -> {
                    // show snackbar
                }
            }
        }
    }
    OrderScreen(
        uiState = uiState,
        onPrimaryAction = { order ->
            viewModel.onPrimaryActionForOrder(order)
        },
        onSecondaryAction = {},
        onViewDetail = { orderId ->
            viewModel.onViewOrderDetail(orderId)
        }
    )

}