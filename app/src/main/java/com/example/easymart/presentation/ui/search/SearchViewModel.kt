package com.example.easymart.presentation.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.repository.SearchRepository
import com.example.easymart.domain.usecase.search.GetSuggestionUseCase
import com.example.easymart.domain.usecase.search.SearchProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductsUC: SearchProductUseCase,
    private val getSuggestionsUC: GetSuggestionUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SearchUiEvent>()
    val event = _event.asSharedFlow()

    fun searchProducts(query: String) {
        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(
                searchQuery = query,
                products = emptyList(),
                suggestions = emptyList(),
                isLoading = false
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                searchQuery = query,
                isLoading = true,
                error = null
            )

            try {
                val products = searchProductsUC(query)
                _uiState.value = _uiState.value.copy(
                    products = products,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Có lỗi xảy ra khi tìm kiếm"
                )
                _event.emit(SearchUiEvent.ShowMessage("Không thể tìm kiếm sản phẩm"))
            }
        }
    }

    private fun getSuggestions(query: String) {
        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(suggestions = emptyList())
            return
        }

        viewModelScope.launch {
            try {
                val suggestions = getSuggestionsUC(query)
                _uiState.value = _uiState.value.copy(suggestions = suggestions)
                Log.d("SearchViewModel", "suggestions: $suggestions")
            } catch (e: Exception) {
                // Không hiển thị lỗi cho suggestions
                _uiState.value = _uiState.value.copy(suggestions = emptyList())
            }
        }
        Log.d("SearchViewModel", "query: $query")
        Log.d("SearchViewModel", "uiState: ${_uiState.value}")
    }

    fun onSearchTextChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        if (query.isNotBlank()) {
            getSuggestions(query)
        } else {
            _uiState.value = _uiState.value.copy(
                suggestions = emptyList(),
                products = emptyList()
            )
        }
    }

    fun onProductClick(product: Product) {
        viewModelScope.launch {
            _event.emit(SearchUiEvent.NavigateToProduct(product))
        }
    }

    fun onSuggestionClick(suggestion: String) {
        _uiState.value = _uiState.value.copy(searchQuery = suggestion)
        searchProducts(suggestion)
    }

    fun clearSearch() {
        _uiState.value = SearchUiState()
    }
}

