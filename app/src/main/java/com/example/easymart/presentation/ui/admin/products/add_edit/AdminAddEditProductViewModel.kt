package com.example.easymart.presentation.ui.admin.products.add_edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.model.ProductRating
import com.example.easymart.domain.usecase.product.GetProductUseCase
import com.example.easymart.domain.usecase.product.UpsertProductUseCase
import com.example.easymart.domain.usecase.ocr.AnalyzeProductImageUseCase
import com.example.easymart.presentation.common.AppEventBus
import com.example.easymart.presentation.common.Resource
import com.example.easymart.presentation.common.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminAddEditProductViewModel @Inject constructor(
    private val upsertProductUseCase: UpsertProductUseCase,
    private val getProductUseCase: GetProductUseCase,
    private val analyzeProductImageUseCase: AnalyzeProductImageUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminAddEditProductUiState())
    val uiState = _uiState.asStateFlow()

    fun onTitleChange(value: String) = updateField { copy(title = value, titleError = null, aiFilledTitle = false) }
    fun onPriceChange(value: String) = updateField { copy(price = value, priceError = null) }
    fun onDescriptionChange(value: String) = updateField { copy(description = value, descriptionError = null, aiFilledDescription = false) }
    fun onCategoryChange(value: String) = updateField { copy(category = value, categoryError = null, aiFilledCategory = false) }
    fun onQuantityChange(value: String) = updateField { copy(quantity = value, quantityError = null) }
    fun onImageUriChange(value: String) = updateField { copy(imageUri = value, imageUriError = null) }

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            getProductUseCase(productId).collectLatest { result ->
                if (result is Resource.Success) {
                    val product = result.data
                    _uiState.update {
                        it.copy(
                            productId = product.id,
                            isEdit = true,
                            createdAt = product.createdAt,
                            ratingRate = product.rating.rate,
                            ratingCount = product.rating.count,
                            isVisible = product.isVisible,
                            storagePath = product.storagePath,
                            title = product.name,
                            price = product.price.toString(),
                            description = product.description.orEmpty(),
                            category = product.category,
                            quantity = product.stockQuantity.toString(),
                            imageUri = product.localImageUri ?: product.imageUrl
                        )
                    }
                }
            }
        }
    }

    fun saveProduct() {
        val current = _uiState.value
        val validated = validate(current)
        if (!validated.first) {
            _uiState.update { validated.second }
            return
        }

        val now = System.currentTimeMillis()
        val id = current.productId ?: now.hashCode()
        val createdAt = if (current.isEdit && current.createdAt > 0L) current.createdAt else now
        val rating = if (current.isEdit) {
            ProductRating(rate = current.ratingRate, count = current.ratingCount)
        } else {
            ProductRating(rate = 0.0, count = 0)
        }

        val imageValue = current.imageUri.trim()
        val isRemoteImage = imageValue.startsWith("http", ignoreCase = true)

        val product = Product(
            id = id,
            name = current.title.trim(),
            description = current.description.trim(),
            price = current.price.trim().toDouble(),
            imageUrl = imageValue,
            localImageUri = if (isRemoteImage) null else imageValue,
            category = current.category.trim(),
            rating = rating,
            isVisible = current.isVisible,
            createdAt = createdAt,
            updatedAt = now,
            storagePath = current.storagePath,
            stockQuantity = current.quantity.trim().toInt()
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

    fun onImagePicked(uri: String) {
        _uiState.update {
            it.copy(
                imageUri = uri,
                imageUriError = null,
                isScanning = false,
                scanError = null,
                scanSuccess = false,
                ocrText = "",
                suggestedTitle = null,
                suggestedCategory = null,
                suggestedDescription = null,
                suggestionConfidence = null,
                aiFilledTitle = false,
                aiFilledCategory = false,
                aiFilledDescription = false
            )
        }
    }

    fun onScanWithAi() {
        val uri = _uiState.value.imageUri.trim()
        if (uri.isBlank()) {
            _uiState.update { it.copy(scanError = "Vui lòng chọn ảnh trước khi quét") }
            return
        }
        startScan(uri)
    }

    fun onRetryScan() {
        val uri = _uiState.value.imageUri.trim()
        if (uri.isBlank()) return
        startScan(uri)
    }

    private fun startScan(uri: String) {
        _uiState.update { it.copy(isScanning = true, scanError = null, scanSuccess = false) }
        viewModelScope.launch {
            runCatching { analyzeProductImageUseCase(uri) }
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            isScanning = false,
                            scanSuccess = true,
                            ocrText = result.rawText,
                            suggestedTitle = result.suggestedTitle,
                            suggestedCategory = result.suggestedCategory,
                            suggestedDescription = result.suggestedDescription,
                            suggestionConfidence = result.suggestionConfidence,
                            title = result.suggestedTitle ?: it.title,
                            category = result.suggestedCategory ?: it.category,
                            description = result.suggestedDescription ?: it.description,
                            aiFilledTitle = result.suggestedTitle != null,
                            aiFilledCategory = result.suggestedCategory != null,
                            aiFilledDescription = result.suggestedDescription != null
                        )
                    }
                    Log.d("AdminAddEditProductViewModel", "AI result: $result")
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isScanning = false,
                            scanError = throwable.message ?: "Không thể quét ảnh",
                            scanSuccess = false
                        )
                    }
                    Log.d("AdminAddEditProductViewModel", "AI scan failed", throwable)
                }
        }
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
            quantityError = null,
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

        val quantity = state.quantity.trim().toIntOrNull()
        if (quantity == null || quantity < 0) {
            next = next.copy(quantityError = "Số lượng phải là số >= 0")
            isValid = false
        }

        if (state.imageUri.trim().isEmpty()) {
            next = next.copy(imageUriError = "Vui lòng chọn ảnh sản phẩm")
            isValid = false
        }

        return isValid to next
    }
}
