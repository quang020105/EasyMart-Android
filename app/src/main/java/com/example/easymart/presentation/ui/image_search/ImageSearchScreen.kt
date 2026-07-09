package com.example.easymart.presentation.ui.image_search

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.FileProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.image_search.components.ImageSearchEmptyState
import com.example.easymart.presentation.ui.image_search.components.ImageSearchErrorState
import com.example.easymart.presentation.ui.image_search.components.ImageSearchHeader
import com.example.easymart.presentation.ui.image_search.components.ImageSearchLoadingState
import com.example.easymart.presentation.ui.image_search.components.ImageSearchPickerCard
import com.example.easymart.presentation.ui.image_search.components.ImageSearchResultGrid
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ImageSearchScreen(
    uiState: ImageSearchUiState,
    onImageSelected: (Uri?) -> Unit,
    onSearchClick: () -> Unit,
    onRetryClick: () -> Unit,
    onClearClick: () -> Unit,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dimens = LocalAppDimens.current
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                onImageSelected(pendingCameraUri)
            }
        }
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> onImageSelected(uri) }
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(dimens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(dimens.spaceMd)
    ) {
        item {
            ImageSearchHeader()
        }

        item {
            ImageSearchPickerCard(
                selectedImage = uiState.selectedImage,
                loading = uiState.isLoading,
                onCameraClick = {
                    val uri = createImageSearchUri(context)
                    pendingCameraUri = uri
                    takePictureLauncher.launch(uri)
                },
                onGalleryClick = { galleryLauncher.launch("image/*") },
                onClearClick = onClearClick,
                onSearchClick = onSearchClick
            )
        }

        item {
            AnimatedVisibility(
                visible = uiState.error != null && !uiState.isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                uiState.error?.let { message ->
                    ImageSearchErrorState(
                        message = message,
                        retryEnabled = uiState.selectedImage != null,
                        onRetryClick = onRetryClick
                    )
                }
            }
        }

        item {
            AnimatedVisibility(
                visible = uiState.isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ImageSearchLoadingState()
            }
        }

        when {
            uiState.products.isNotEmpty() && !uiState.isLoading -> item {
                ImageSearchResultGrid(
                    products = uiState.products,
                    onProductClick = onProductClick
                )
            }

            uiState.error == null && !uiState.isLoading -> item {
                ImageSearchEmptyState(hasSelectedImage = uiState.selectedImage != null)
            }
        }
    }
}

private fun createImageSearchUri(context: Context): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val imageDir = File(context.cacheDir, "images").apply { mkdirs() }
    val imageFile = File(imageDir, "IMAGE_SEARCH_$timeStamp.jpg")
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        imageFile
    )
}

@Preview
@Composable
fun ImageSearchScreenPreview() {
    EasyMartTheme {
        ImageSearchScreen(
            uiState = ImageSearchUiState(
                selectedImage = null,
                isLoading = false,
                products = emptyList(),
                error = null
            ),
            onImageSelected = {},
            onSearchClick = {},
            onRetryClick = {},
            onClearClick = {},
            onProductClick = {}
        )
    }
}

