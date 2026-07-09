package com.example.easymart.presentation.ui.image_search

import android.net.Uri
import com.example.easymart.domain.model.image_search.SimilarProduct

data class ImageSearchUiState(
    val selectedImage: Uri? = null,
    val isLoading: Boolean = false,
    val products: List<SimilarProduct> = emptyList(),
    val error: String? = null
)
