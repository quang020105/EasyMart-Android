package com.example.easymart.presentation.ui.admin.products.add_edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun AdminAddEditProductRoute(
    viewModel: AdminAddEditProductViewModel = hiltViewModel(),
    productId: Int? = null,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(productId) {
        if (productId != null && productId > 0) {
            viewModel.loadProduct(productId)
        }
    }

    AdminAddEditProductScreen(
        uiState = uiState,
        onTitleChange = viewModel::onTitleChange,
        onPriceChange = viewModel::onPriceChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onCategoryChange = viewModel::onCategoryChange,
        onImageSelected = viewModel::onImageUriChange,
        onSave = viewModel::saveProduct,
        onNavigateBack = onNavigateBack
    )

    if (uiState.success) {
        LaunchedEffect(true) {
            viewModel.resetSuccess()
            onNavigateBack()
        }
    }
}
