package com.example.easymart.domain.usecase.image_search

import android.net.Uri
import com.example.easymart.domain.model.image_search.ImageSearchResult
import com.example.easymart.domain.repository.ImageSearchRepository
import com.example.easymart.presentation.common.Resource
import javax.inject.Inject

class SearchByImageUseCase @Inject constructor(
    private val repository: ImageSearchRepository
) {
    suspend operator fun invoke(uri: Uri): Resource<ImageSearchResult> =
        repository.searchByImage(uri)
}
