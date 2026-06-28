package com.example.easymart.presentation.ui.productdetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.model.CartItem
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.usecase.auth.GetCurrentUserUseCase
import com.example.easymart.domain.usecase.cart.AddToCartUseCase
import com.example.easymart.domain.usecase.product.GetAllProductUseCase
import com.example.easymart.domain.usecase.product.GetProductUseCase
import com.example.easymart.presentation.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val getAllProductUseCase: GetAllProductUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ProductDetailUiEvent>()
    val event = _event.asSharedFlow()

    fun loadProduct(productId: Int) {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            isRefreshing = true,
            error = null,
            refreshError = null
        )
        observeProduct(productId)
        refreshProduct(productId)
    }

    private fun observeProduct(productId: Int) {
        viewModelScope.launch {
            val result = getProductUseCase(productId)
            result.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }

                    is Resource.Success -> {
                        val product = resource.data
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            product = product,
                            error = null
                        )
                        //load các sản phẩm tương tự
                        similarProducts(product)
                    }

                    is Resource.Error -> {
                        val currentState = _uiState.value
                        _uiState.value = _uiState.value.copy(
                            isLoading = currentState.product == null && currentState.isRefreshing,
                            product = currentState.product,
                            error = resource.message
                        )
                    }
                }
            }
        }
    }

    private fun refreshProduct(productId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true, refreshError = null)
            when (val result = getProductUseCase.refresh(productId)) {
                is Resource.Success -> _uiState.value = _uiState.value.copy(isRefreshing = false)
                is Resource.Error -> _uiState.value = _uiState.value.copy(
                    isRefreshing = false,
                    isLoading = false,
                    refreshError = result.message
                )
                is Resource.Loading -> Unit
            }
        }
    }

    private fun similarProducts(product: Product) {
        viewModelScope.launch {
            val resource = getAllProductUseCase()
            resource.collect { res ->
                when (res) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoadingSimilar = true)
                    }

                    is Resource.Success -> {
                        val data = res.data
                        val candidates = data.filter { it.id != product.id }
                        getAndSortSimilarProducts(candidates, product)
                    }

                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoadingSimilar = false,
                            error = res.message,
                            similarProducts = emptyList()
                        )
                    }
                }

            }
        }
    }

    //xử lý logic lấy và sắp xếp các sản phẩm tương tự
    private fun getAndSortSimilarProducts(candidates: List<Product>, product: Product) {
        val scored = candidates.map { candidate ->
            var score = 0
            if (product.category == candidate.category) {
                score += 50
            }
            if (product.price > 0) {
                val priceDiffRatio = (abs(product.price - candidate.price) / product.price)
                if (priceDiffRatio <= 0.2) {
                    score += 20
                }
            }
            Log.d("ProductDetailViewModel", "score: $score")
            Pair(candidate, score)

        }

        val sorted = scored.sortedWith(compareByDescending<Pair<Product, Int>> { it.second }
            .thenBy { it.first.id })
            .map { it.first }
        _uiState.value = _uiState.value.copy(similarProducts = sorted, isLoadingSimilar = false)
    }

    fun addProductToCart(product: Product, quantity: Int = 1) {
//        viewModelScope.launch {
//            try {
//                val newCartItem = CartItem(
//                    id = product.id,
//                    product = product,
//                    quantity = quantity,
//                    price = product.price,
//                )
//                addToCartUseCase(newCartItem)
//                _event.emit(ProductDetailUiEvent.ShowMessage("Đã thêm vào giỏ hàng"))
//            } catch (e: Exception) {
//                _event.emit(ProductDetailUiEvent.ShowMessage("Không thể thêm sản phẩm vào giỏ hàng"))
//                Log.e("CartViewModelError", "${e.message}")
//            }
//        }

        viewModelScope.launch {
            val userId = getCurrentUserUseCase()?.id
            runCatching {
                val newCartItem = CartItem(
                    id = product.id,
                    product = product,
                    quantity = quantity,
                    price = product.price,
                )
                addToCartUseCase(userId, newCartItem)
            }.onSuccess {
                _event.emit(ProductDetailUiEvent.ShowMessage("Đã thêm vào giỏ hàng"))
            }.onFailure {
                _event.emit(ProductDetailUiEvent.ShowMessage("Không thể thêm sản phẩm vào giỏ hàng"))
                Log.e("CartViewModelError", "${it.message}")
            }
        }
    }
}
