package com.example.easymart.domain.repository

import android.net.Uri
import com.example.easymart.domain.model.image_search.ImageSearchResult
import com.example.easymart.presentation.common.Resource

interface ImageSearchRepository {
    suspend fun searchByImage(uri: Uri): Resource<ImageSearchResult>
}
