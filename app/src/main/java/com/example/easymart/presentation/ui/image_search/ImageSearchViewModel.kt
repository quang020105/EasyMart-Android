package com.example.easymart.presentation.ui.image_search

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.usecase.image_search.SearchByImageUseCase
import com.example.easymart.presentation.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageSearchViewModel @Inject constructor(
    private val searchByImageUseCase: SearchByImageUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ImageSearchUiState())
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onImageSelected(uri: Uri?) {
        searchJob?.cancel()
        _uiState.update {
            it.copy(
                selectedImage = uri,
                isLoading = false,
                products = emptyList(),
                error = null
            )
        }
    }

    fun search() {
        if (_uiState.value.isLoading) return

        val selectedImage = _uiState.value.selectedImage
        if (selectedImage == null) {
            _uiState.update { it.copy(error = "Vui lòng chọn ảnh trước khi tìm kiếm") }
            return
        }

        searchByImage(selectedImage)
    }

    fun retry() {
        if (_uiState.value.isLoading) return

        val selectedImage = _uiState.value.selectedImage ?: return
        searchByImage(selectedImage)
    }

    fun clear() {
        searchJob?.cancel()
        _uiState.value = ImageSearchUiState()
    }

    private fun searchByImage(uri: Uri) {
        if (_uiState.value.isLoading) return

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = searchByImageUseCase(uri)) {
                is Resource.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        products = result.data.products,
                        error = null
                    )
                }

                is Resource.Error -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = result.message ?: "Không thể tìm kiếm bằng hình ảnh"
                    )
                }

                is Resource.Loading -> _uiState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        }
    }
}
