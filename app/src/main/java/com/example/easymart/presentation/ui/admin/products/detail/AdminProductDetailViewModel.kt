package com.example.easymart.presentation.ui.admin.products.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.usecase.product.GetProductUseCase
import com.example.easymart.domain.usecase.product.UpsertProductUseCase
import com.example.easymart.domain.usecase.product.UpdateProductVisibilityLocalOnlyUseCase
import com.example.easymart.presentation.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminProductDetailViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val upsertProductUseCase: UpsertProductUseCase,
    private val updateVisibilityLocalOnlyUseCase: UpdateProductVisibilityLocalOnlyUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminProductDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            getProductUseCase(productId).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true, error = null) }
                    is Resource.Success -> _uiState.update {
                        it.copy(isLoading = false, product = resource.data, error = null)
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(isLoading = false, product = null, error = resource.message)
                    }
                }
            }
        }
    }

    fun toggleVisibility(visible: Boolean) {
        val product = _uiState.value.product ?: return
        viewModelScope.launch {
            val isFromApi = product.storagePath.isNullOrBlank() && product.localImageUri.isNullOrBlank()
            if (isFromApi) {
                updateVisibilityLocalOnlyUseCase.update(product.id, visible)
                return@launch
            }

            val updated = product.copy(
                isVisible = visible,
                updatedAt = System.currentTimeMillis()
            )
            upsertProductUseCase.upsertProduct(updated)
        }
    }
}

