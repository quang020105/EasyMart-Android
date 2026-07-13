package com.example.easymart.presentation.ui.admin.products.add_edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.model.ProductImage
import com.example.easymart.domain.model.ProductRating
import com.example.easymart.domain.usecase.product.GetAllProductUseCase
import com.example.easymart.domain.usecase.product.GetProductUseCase
import com.example.easymart.domain.usecase.product.UpsertProductUseCase
import com.example.easymart.domain.usecase.ocr.AnalyzeProductImageUseCase
import com.example.easymart.presentation.common.AppEventBus
import com.example.easymart.presentation.common.Resource
import com.example.easymart.presentation.common.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminAddEditProductViewModel @Inject constructor(
    private val upsertProductUseCase: UpsertProductUseCase,
    private val getProductUseCase: GetProductUseCase,
    private val analyzeProductImageUseCase: AnalyzeProductImageUseCase,
    private val getAllProductUseCase: GetAllProductUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminAddEditProductUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<AdminAddEditProductUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        observeCategories()
    }

    fun onTitleChange(value: String) =
        updateField { copy(title = value, titleError = null, aiFilledTitle = false) }

    fun onBrandChange(value: String) =
        updateField { copy(brand = value, brandError = null) }

    fun onPriceChange(value: String) = updateField { copy(price = value, priceError = null) }
    fun onDescriptionChange(value: String) = updateField {
        copy(
            description = value,
            descriptionError = null,
            aiFilledDescription = false
        )
    }

    fun onCategoryChange(value: String) = updateField {
        copy(category = value, categoryError = null, aiFilledCategory = false)
    }

    fun onQuantityChange(value: String) =
        updateField { copy(quantity = value, quantityError = null) }

    fun onImageUriChange(value: String) =
        updateField { copy(mainImageUri = value, imageUriError = null) }

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            getProductUseCase(productId).collectLatest { result ->
                if (result is Resource.Success) {
                    val product = result.data

                    val secondaryImages = buildList {
                        product.imageUrls.filter { it.isNotBlank() }.forEach { if (!contains(it)) add(it) }
                        product.images.map { it.imageUrl }.filter { it.isNotBlank() }
                            .forEach { if (!contains(it)) add(it) }
                        product.localImageUris.filter { it.isNotBlank() }
                            .forEach { if (!contains(it)) add(it) }
                    }

                    val mainImage = product.localImageUri?.takeIf { it.isNotBlank() }
                        ?: product.imageUrl.takeIf { it.isNotBlank() }
                        ?: ""

                    _uiState.update {
                        it.copy(
                            productId = product.id,
                            isEdit = true,
                            createdAt = product.createdAt,
                            ratingRate = product.rating.rate,
                            ratingCount = product.rating.count,
                            soldQuantity = product.soldQuantity,
                            isVisible = product.isVisible,
                            storagePath = product.storagePath,
                            title = product.name,
                            brand = product.brand,
                            price = product.price.toString(),
                            description = product.description.orEmpty(),
                            category = product.category,
                            quantity = product.stockQuantity.toString(),
                            mainImageUri = mainImage,
                            imageUris = secondaryImages,
                            selectedImageIndex = 0,
                            scanImageIndex = if (secondaryImages.isEmpty()) null else 0,
                            scanImageUri = secondaryImages.firstOrNull()
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
            _uiState.update {
                validated.second.copy(validationAttempt = it.validationAttempt + 1)
            }
            viewModelScope.launch {
                _uiEvent.send(
                    AdminAddEditProductUiEvent.ShowMessage(
                        validated.second.error ?: "Vui lòng nhập đầy đủ các thông tin cần thiết"
                    )
                )
            }
            return
        }

        val normalizedCategory = current.category.trim()
        if (normalizedCategory.isNotBlank()) {
            _uiState.update { state ->
                state.copy(categories = (state.categories + normalizedCategory).distinct().sorted())
            }
        }

        val now = System.currentTimeMillis()
        val id = current.productId ?: now.hashCode()
        val createdAt = if (current.isEdit && current.createdAt > 0L) current.createdAt else now
        val rating = if (current.isEdit) {
            ProductRating(rate = current.ratingRate, count = current.ratingCount)
        } else {
            ProductRating(rate = 0.0, count = 0)
        }

        val mainUri = current.mainImageUri.trim()
        val (remoteSecondary, localSecondary) = splitImageUris(current.imageUris)
        val isMainRemote = isRemoteUri(mainUri)

        val product = Product(
            id = id,
            name = current.title.trim(),
            brand = current.brand.trim(),
            description = current.description.trim(),
            price = current.price.trim().toDouble(),
            imageUrl = if (isMainRemote) mainUri else "",
            localImageUri = if (isMainRemote) null else mainUri,
            imageUrls = remoteSecondary,
            localImageUris = localSecondary,
            category = current.category.trim(),
            rating = rating,
            soldQuantity = current.soldQuantity,
            isVisible = current.isVisible,
            createdAt = createdAt,
            updatedAt = now,
            storagePath = current.storagePath,
            stockQuantity = current.quantity.trim().toInt(),
            images = remoteSecondary.mapIndexed { index, uri ->
                ProductImage(
                    id = "$id-$index",
                    productId = id.toString(),
                    imageUrl = uri
                )
            }
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = upsertProductUseCase.upsertProduct(product)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    AppEventBus.send(
                        UiEvent.ShowMessage(
                            if (current.isEdit) "Sửa sản phẩm thành công" else "Thêm sản phẩm thành công"
                        )
                    )
                    _uiEvent.send(AdminAddEditProductUiEvent.NavigateBack)
                }

                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                    _uiEvent.send(
                        AdminAddEditProductUiEvent.ShowMessage(
                            result.message ?: "Không thể lưu sản phẩm"
                        )
                    )
                }

                is Resource.Loading -> Unit
            }
        }
    }

    fun onImagePicked(uri: String) {
        if (uri.isBlank()) return
        _uiState.update { state ->
            val hasMain = state.mainImageUri.isNotBlank()
            val nextSecondary = if (hasMain) {
                (state.imageUris + uri).distinct()
            } else {
                state.imageUris
            }
            val nextMain = if (hasMain) state.mainImageUri else uri

            state.copy(
                mainImageUri = nextMain,
                imageUris = nextSecondary,
                selectedImageIndex = 0,
                scanImageIndex = if (nextSecondary.isEmpty()) null else 0,
                scanImageUri = nextSecondary.firstOrNull(),
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

    fun onImagesPicked(uris: List<String>) {
        val validUris = uris.filter { it.isNotBlank() }
        if (validUris.isEmpty()) return
        _uiState.update { state ->
            val hasMain = state.mainImageUri.isNotBlank()
            val newMain = if (!hasMain) validUris.first() else state.mainImageUri
            val newSecondary = if (!hasMain) validUris.drop(1) else validUris
            val nextSecondary = (state.imageUris + newSecondary).distinct()

            state.copy(
                mainImageUri = newMain,
                imageUris = nextSecondary,
                selectedImageIndex = 0,
                scanImageIndex = if (nextSecondary.isEmpty()) null else 0,
                scanImageUri = nextSecondary.firstOrNull(),
                imageUriError = null
            )
        }
    }

    fun onSelectImage(index: Int) {
        _uiState.update { state ->
            if (state.imageUris.isEmpty()) return@update state
            val safeIndex = index.coerceIn(0, state.imageUris.lastIndex)
            val selectedUri = state.imageUris.getOrNull(safeIndex).orEmpty()
            if (selectedUri.isBlank()) return@update state

            val previousMain = state.mainImageUri
            val nextSecondary = state.imageUris.toMutableList().apply {
                removeAt(safeIndex)
                if (previousMain.isNotBlank()) add(previousMain)
            }.distinct()

            state.copy(
                mainImageUri = selectedUri,
                imageUris = nextSecondary,
                selectedImageIndex = 0,
                scanImageIndex = if (nextSecondary.isEmpty()) null else 0,
                scanImageUri = nextSecondary.firstOrNull()
            )
        }
    }

    fun onDeleteImage(index: Int) {
        _uiState.update { state ->
            if (state.imageUris.isEmpty()) return@update state
            val safeIndex = index.coerceIn(0, state.imageUris.lastIndex)
            val nextList = state.imageUris.toMutableList().apply { removeAt(safeIndex) }
            state.copy(
                imageUris = nextList,
                selectedImageIndex = 0,
                scanImageIndex = if (nextList.isEmpty()) null else 0,
                scanImageUri = nextList.firstOrNull(),
                imageUriError = if (state.mainImageUri.isBlank()) "Vui lòng chọn ảnh sản phẩm" else null
            )
        }
    }

    fun onDeleteMainImage() {
        _uiState.update { state ->
            if (state.mainImageUri.isBlank()) return@update state
            val nextMain = state.imageUris.firstOrNull().orEmpty()
            val nextSecondary = if (nextMain.isBlank()) {
                emptyList()
            } else {
                state.imageUris.drop(1)
            }
            state.copy(
                mainImageUri = nextMain,
                imageUris = nextSecondary,
                selectedImageIndex = 0,
                scanImageIndex = if (nextSecondary.isEmpty()) null else 0,
                scanImageUri = nextSecondary.firstOrNull(),
                imageUriError = if (nextMain.isBlank()) "Vui lòng chọn ảnh chính" else null
            )
        }
    }

    fun onScanImageSelected(uri: String) {
        _uiState.update { state ->
            if (uri == state.mainImageUri) {
                return@update state.copy(scanImageIndex = null, scanImageUri = uri)
            }
            val index = state.imageUris.indexOf(uri)
            if (index < 0) return@update state
            state.copy(scanImageIndex = index, scanImageUri = uri)
        }
    }

    fun onScanWithAi() {
        val state = _uiState.value
        val uri = (state.scanImageUri ?: state.mainImageUri).orEmpty().trim()
        if (uri.isBlank()) {
            _uiState.update { it.copy(scanError = "Vui lòng chọn ảnh trước khi quét") }
            return
        }
        startScan(uri)
    }

    fun onRetryScan() {
        val state = _uiState.value
        val uri = (state.scanImageUri ?: state.mainImageUri).orEmpty().trim()
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

    private fun updateField(updater: AdminAddEditProductUiState.() -> AdminAddEditProductUiState) {
        _uiState.update { it.updater() }
    }

    private fun validate(state: AdminAddEditProductUiState): Pair<Boolean, AdminAddEditProductUiState> {
        var next = state.copy(
            titleError = null,
            brandError = null,
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
        } else if (state.title.trim().length > 100) {
            next = next.copy(titleError = "Tiêu đề không được vượt quá 100 ký tự")
            isValid = false
        }

        if (state.brand.trim().length > 60) {
            next = next.copy(brandError = "Thương hiệu không được vượt quá 60 ký tự")
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
        } else if (state.description.trim().length > 1000) {
            next = next.copy(descriptionError = "Mô tả không được vượt quá 1000 ký tự")
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

        if (state.mainImageUri.isBlank()) {
            next = next.copy(imageUriError = "Vui lòng chọn ảnh chính")
            isValid = false
        }

        if (state.imageUris.size > 5) {
            next = next.copy(imageUriError = "Không được chọn quá 5 ảnh phụ")
            isValid = false
        }

        if (!isValid) {
            next = next.copy(error = "Vui lòng nhập đầy đủ các thông tin cần thiết")
        }

        return isValid to next
    }

    private fun observeCategories() {
        viewModelScope.launch {
            getAllProductUseCase().collectLatest { resource ->
                val products = (resource as? Resource.Success)?.data.orEmpty()
                if (products.isEmpty()) return@collectLatest
                val categories = products.map { it.category.trim() }
                    .filter { it.isNotBlank() }
                    .distinct()
                    .sorted()
                _uiState.update { state ->
                    val merged = (state.categories + categories).distinct().sorted()
                    state.copy(categories = merged)
                }
            }
        }
    }

    private fun splitImageUris(imageUris: List<String>): Pair<List<String>, List<String>> {
        val remote = imageUris.filter { isRemoteUri(it) }
            .distinctBy { normalizeRemoteKey(it) }
        val local = imageUris.filterNot { isRemoteUri(it) }
            .distinct()
        return remote to local
    }

    private fun isRemoteUri(uri: String): Boolean =
        uri.startsWith("http://", ignoreCase = true) || uri.startsWith("https://", ignoreCase = true)

    private fun normalizeRemoteKey(uri: String): String = uri.substringBefore("?")
}
