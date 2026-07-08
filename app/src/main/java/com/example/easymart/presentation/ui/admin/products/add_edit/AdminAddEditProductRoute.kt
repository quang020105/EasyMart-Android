package com.example.easymart.presentation.ui.admin.products.add_edit

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AdminAddEditProductRoute(
    viewModel: AdminAddEditProductViewModel = hiltViewModel(),
    productId: Int? = null,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Log.d("AdminAddEditProductRoute", "imageUri: ${uiState.mainImageUri}, imagesUri: ${uiState.imageUris}")

    LaunchedEffect(productId) {
        if (productId != null && productId > 0) {
            viewModel.loadProduct(productId)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is AdminAddEditProductUiEvent.ShowMessage -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(event.message)
                    }
                }
                AdminAddEditProductUiEvent.NavigateBack -> onNavigateBack()
            }
        }
    }

    AdminAddEditProductScreen(
        uiState = uiState,
        onTitleChange = viewModel::onTitleChange,
        onBrandChange = viewModel::onBrandChange,
        onPriceChange = viewModel::onPriceChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onCategoryChange = viewModel::onCategoryChange,
        onImageSelected = viewModel::onImagePicked,
        onImagesSelected = viewModel::onImagesPicked,
        onSelectImage = viewModel::onSelectImage,
        onDeleteImage = viewModel::onDeleteImage,
        onDeleteMainImage = viewModel::onDeleteMainImage,
        onScanImageSelected = viewModel::onScanImageSelected,
        onSave = viewModel::saveProduct,
        onNavigateBack = onNavigateBack,
        onQuantityChange = viewModel::onQuantityChange,
        onScanWithAi = viewModel::onScanWithAi,
        onRetryScan = viewModel::onRetryScan,
        snackbarHostState = snackbarHostState
    )
}
