package com.example.easymart.presentation.ui.orderdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun OrderDetailRoute(
    viewModel: OrderDetailViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                is OrderDetailUiEvent.NavigateToBuyAgain -> {

                }

                is OrderDetailUiEvent.NavigateToTrack -> {
                }

                is OrderDetailUiEvent.NavigateToReview -> {
                }

                is OrderDetailUiEvent.ShowToast -> {
                    //show snackbar
                }

                is OrderDetailUiEvent.ShowConfirmCancel -> {
                    //show snackbar
                }
            }
        }
    }
    OrderDetailScreen(
        uiState = uiState
    )
}