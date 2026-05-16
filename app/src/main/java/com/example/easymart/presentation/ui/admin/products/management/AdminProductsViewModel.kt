package com.example.easymart.presentation.ui.admin.products.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.usecase.product.GetAllProductUseCase
import com.example.easymart.domain.usecase.product.UpsertProductUseCase
import com.example.easymart.domain.usecase.product.UpdateProductVisibilityLocalOnlyUseCase
import com.example.easymart.domain.usecase.product.SyncProductsUseCase
import com.example.easymart.presentation.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminProductsViewModel @Inject constructor(
    private val getAllProductUseCase: GetAllProductUseCase,
    private val upsertProductUseCase: UpsertProductUseCase,
    private val updateVisibilityLocalOnlyUseCase: UpdateProductVisibilityLocalOnlyUseCase,
    private val syncProductsUseCase: SyncProductsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminProductsUiState())
    val uiState = _uiState.asStateFlow()
    private var didInitialSync = false

    init {
        observeProducts()
        refresh()
        syncOnceOnFirstEntry()
    }

    //đồng bộ dữ liệu local <-> remote khi vào app lần đầu
    private fun syncOnceOnFirstEntry() {
        if (didInitialSync) return
        didInitialSync = true
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = syncProductsUseCase.sync()) {
                is Resource.Success -> _uiState.update { it.copy(isLoading = false) }
                is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                is Resource.Loading -> Unit
            }
        }
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

    fun onSelectSource(filter: ProductSourceFilter) {
        _uiState.update { it.copy(sourceFilter = filter) }
    }

    fun onSortClick() {
        val next = when (_uiState.value.sortType) {
            AdminProductSort.UPDATED_DESC -> AdminProductSort.NAME_ASC
            AdminProductSort.NAME_ASC -> AdminProductSort.PRICE_ASC
            AdminProductSort.PRICE_ASC -> AdminProductSort.UPDATED_DESC
        }
        _uiState.update { it.copy(sortType = next) }
    }

    fun onSelectSort(sort: AdminProductSort) {
        _uiState.update { it.copy(sortType = sort) }
    }

    fun onToggleVisibility(product: Product, visible: Boolean) {
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