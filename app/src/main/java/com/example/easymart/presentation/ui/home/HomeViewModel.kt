package com.example.easymart.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.usecase.product.GetAllProductUseCase
import com.example.easymart.presentation.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProducts: GetAllProductUseCase
) : ViewModel() {
    //cập nhật UI nóng
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    //xử lý sự kiện 1 lần
    private val _event = MutableSharedFlow<HomeUiEvent>()
    val event = _event.asSharedFlow()

    init {
        observeProducts()
        refreshProducts()
    }

    private fun observeProducts() {
        viewModelScope.launch {
            getProducts().collect { resource ->
                when (resource) {
                    is Resource.Loading -> _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                    is Resource.Success -> _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        products = resource.data,
                        error = null
                    )
                    is Resource.Error -> _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = resource.message
                    )
                }
            }
        }
    }

    fun refreshProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true, refreshError = null)
            when (val result = getProducts.refresh()) {
                is Resource.Success -> _uiState.value = _uiState.value.copy(isRefreshing = false)
                is Resource.Error -> _uiState.value = _uiState.value.copy(
                    isRefreshing = false,
                    refreshError = result.message
                )
                is Resource.Loading -> Unit
            }
        }
    }

    fun onProductClick(product: Product) {
        viewModelScope.launch {
            _event.emit(HomeUiEvent.NavigateToProduct(product))
        }
    }
}