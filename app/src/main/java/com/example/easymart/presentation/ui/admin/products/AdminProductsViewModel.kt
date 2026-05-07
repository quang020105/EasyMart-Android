package com.example.easymart.presentation.ui.admin.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.usecase.product.GetAllProductUseCase
import com.example.easymart.presentation.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminProductsViewModel @Inject constructor(
    private val getAllProductUseCase: GetAllProductUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminProductsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeProducts()
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getAllProductUseCase.refresh()) {
                is Resource.Success -> _uiState.update { it.copy(isLoading = false) }
                is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                is Resource.Loading -> Unit
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onSelectCategory(category: String?) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onToggleLowStock(checked: Boolean) {
        _uiState.update { it.copy(onlyLowStock = checked) }
    }

    private fun observeProducts() {
        viewModelScope.launch {
            getAllProductUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true, error = null) }
                    is Resource.Success -> {
                        val products = resource.data
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                products = products,
                                categories = products.map(Product::category).distinct().sorted(),
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = resource.message) }
                }
            }
        }
    }
}

