package com.example.easymart.presentation.ui.admin.products.add_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.model.ProductRating
import com.example.easymart.domain.usecase.product.GetAllProductUseCase
import com.example.easymart.domain.usecase.product.UpsertProductUseCase
import com.example.easymart.presentation.common.AppEventBus
import com.example.easymart.presentation.common.Resource
import com.example.easymart.presentation.common.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminAddEditProductViewModel @Inject constructor(
    private val upsertProductUseCase: UpsertProductUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminAddEditProductUiState())
    val uiState = _uiState.asStateFlow()

    fun onTitleChange(value: String) = updateField { copy(title = value, titleError = null) }
    fun onPriceChange(value: String) = updateField { copy(price = value, priceError = null) }
    fun onDescriptionChange(value: String) = updateField { copy(description = value, descriptionError = null) }
    fun onCategoryChange(value: String) = updateField { copy(category = value, categoryError = null) }
    fun onImageUriChange(value: String) = updateField { copy(imageUri = value, imageUriError = null) }

    fun saveProduct() {
        val current = _uiState.value
        val validated = validate(current)
        if (!validated.first) {
            _uiState.update { validated.second }
            return
        }

        val now = System.currentTimeMillis()
        val product = Product(
            id = now.hashCode(),
            name = current.title.trim(),
            description = current.description.trim(),
            price = current.price.trim().toDouble(),
            imageUrl = current.imageUri.trim(),
            localImageUri = current.imageUri.trim(),
            category = current.category.trim(),
            rating = ProductRating(rate = 0.0, count = 0),
            isVisible = true,
            createdAt = now,
            updatedAt = now
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = upsertProductUseCase.upsertProduct(product)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isLoading = false, success = true) }
                    AppEventBus.send(UiEvent.ShowMessage("Đã lưu sản phẩm"))
                }
                is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                is Resource.Loading -> Unit
            }
        }
    }

    fun resetSuccess() {
        _uiState.update { it.copy(success = false) }
    }

    private fun updateField (updater: AdminAddEditProductUiState.() -> AdminAddEditProductUiState) {
        _uiState.update { it.updater() }
    }

    private fun validate(state: AdminAddEditProductUiState): Pair<Boolean, AdminAddEditProductUiState> {
        var next = state.copy(
            titleError = null,
            priceError = null,
            descriptionError = null,
            categoryError = null,
            imageUriError = null,
            error = null
        )

        var isValid = true

        if (state.title.trim().length < 3) {
            next = next.copy(titleError = "Tiêu đề tối thiểu 3 ký tự")
            isValid = false
        }

        val price = state.price.trim().toDoubleOrNull()
        if (price == null || price <= 0) {
            next = next.copy(priceError = "Giá phải là số > 0")
            isValid = false
        }

        if (state.description.trim().length < 10) {
            next = next.copy(descriptionError = "Mô tả tối thiểu 10 ký tự")
            isValid = false
        }

        if (state.category.trim().isEmpty()) {
            next = next.copy(categoryError = "Vui lòng nhập danh mục")
            isValid = false
        }

        if (state.imageUri.trim().isEmpty()) {
            next = next.copy(imageUriError = "Vui lòng chọn ảnh sản phẩm")
            isValid = false
        }

        return isValid to next
    }
}
