package com.example.easymart.presentation.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.model.CartItem
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.usecase.auth.GetCurrentUserUseCase
import com.example.easymart.domain.usecase.cart.AddToCartUseCase
import com.example.easymart.domain.usecase.product.GetAllProductUseCase
import com.example.easymart.presentation.common.Resource
import com.example.easymart.presentation.ui.category.components.CustomerProductSort
import com.example.easymart.presentation.ui.category.components.CustomerProductStockFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getAllProductUseCase: GetAllProductUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CategoryUiEvent>()
    val event = _event.asSharedFlow()

    init {
        observeProducts()
        refreshProducts()
    }

    fun refreshProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, refreshError = null) }
            when (val result = getAllProductUseCase.refresh()) {
                is Resource.Success -> _uiState.update { it.copy(isRefreshing = false) }
                is Resource.Error -> _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        isLoading = false,
                        refreshError = result.message
                    )
                }
                is Resource.Loading -> Unit
            }
        }
    }

    fun onCategorySelected(category: String?) {
        _uiState.updateAndApplyFilters { it.copy(selectedCategory = category) }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.updateAndApplyFilters { it.copy(searchQuery = query) }
    }

    fun onSortSelected(sort: CustomerProductSort) {
        _uiState.updateAndApplyFilters { it.copy(sort = sort) }
    }

    fun onStockFilterSelected(filter: CustomerProductStockFilter) {
        _uiState.updateAndApplyFilters { it.copy(stockFilter = filter) }
    }

    fun onProductClick(product: Product) {
        viewModelScope.launch {
            _event.emit(CategoryUiEvent.NavigateToProduct(product))
        }
    }

    fun addProductToCart(product: Product, quantity: Int = 1) {
        viewModelScope.launch {
            val userId = getCurrentUserUseCase()?.id
            runCatching {
                addToCartUseCase(
                    userId,
                    CartItem(
                        id = product.id,
                        product = product,
                        quantity = quantity,
                        price = product.price
                    )
                )
            }.onSuccess {
                _event.emit(CategoryUiEvent.ShowMessage("Đã thêm vào giỏ hàng"))
            }.onFailure {
                _event.emit(CategoryUiEvent.ShowMessage("Không thể thêm sản phẩm vào giỏ hàng"))
            }
        }
    }

    private fun observeProducts() {
        viewModelScope.launch {
            getAllProductUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> _uiState.update {
                        it.copy(isLoading = it.allProducts.isEmpty(), error = null)
                    }
                    is Resource.Success -> {
                        val activeProducts = resource.data
                            .filter { it.isVisible && !it.isDeleted }
                        _uiState.updateAndApplyFilters {
                            it.copy(
                                isLoading = false,
                                allProducts = activeProducts,
                                categories = activeProducts
                                    .map(Product::category)
                                    .filter(String::isNotBlank)
                                    .distinct()
                                    .sorted(),
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(isLoading = false, error = resource.message)
                    }
                }
            }
        }
    }

    private fun MutableStateFlow<CategoryUiState>.updateAndApplyFilters(
        transform: (CategoryUiState) -> CategoryUiState
    ) {
        update { state ->
            val next = transform(state)
            next.copy(products = next.filteredProducts())
        }
    }

    private fun CategoryUiState.filteredProducts(): List<Product> {
        val normalizedQuery = searchQuery.trim()
        return allProducts
            .asSequence()
            .filter { product ->
                selectedCategory == null || product.category == selectedCategory
            }
            .filter { product ->
                normalizedQuery.isBlank() ||
                    product.name.contains(normalizedQuery, ignoreCase = true) ||
                    product.description.orEmpty().contains(normalizedQuery, ignoreCase = true)
            }
            .filter { product ->
                when (stockFilter) {
                    CustomerProductStockFilter.ALL -> true
                    CustomerProductStockFilter.IN_STOCK -> product.stockQuantity > 0
                    CustomerProductStockFilter.OUT_OF_STOCK -> product.stockQuantity <= 0
                }
            }
            .let { products ->
                when (sort) {
                    CustomerProductSort.FEATURED -> products.sortedWith(
                        compareByDescending<Product> { it.rating.rate }
                            .thenByDescending { it.soldQuantity }
                            .thenBy { it.name }
                    )
                    CustomerProductSort.NAME_ASC -> products.sortedBy { it.name }
                    CustomerProductSort.PRICE_ASC -> products.sortedBy { it.price }
                    CustomerProductSort.PRICE_DESC -> products.sortedByDescending { it.price }
                    CustomerProductSort.SOLD_DESC -> products.sortedByDescending { it.soldQuantity }
                }
            }
            .toList()
    }
}
