package com.example.easymart.presentation.ui.admin.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.easymart.presentation.ui.admin.dashboard.AdminDashboardViewModel

@Composable
fun AdminDashboardRoute(
    viewModel: AdminDashboardViewModel = hiltViewModel(),
    onNavigateOrders: () -> Unit,
    onNavigateProducts: () -> Unit,
    onNavigateCategories: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    AdminDashboardScreen(
        uiState = uiState,
        onRefresh = { viewModel.refresh() },
        onSelectPeriod = viewModel::selectPeriod,
        onImportFakeStoreProducts = { viewModel.importFakeStoreProducts() },
        onNavigateOrders = onNavigateOrders,
        onNavigateProducts = onNavigateProducts,
        onNavigateCategories = onNavigateCategories
    )
}

