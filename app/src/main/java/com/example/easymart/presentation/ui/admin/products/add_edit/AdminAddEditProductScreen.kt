package com.example.easymart.presentation.ui.admin.products.add_edit

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.content.FileProvider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import com.example.easymart.presentation.ui.admin.products.add_edit.components.AiImagePickerBottomSheet
import com.example.easymart.presentation.ui.admin.products.add_edit.components.AiScanErrorCard
import com.example.easymart.presentation.ui.admin.products.add_edit.components.AiScanSuccessCard
import com.example.easymart.presentation.ui.admin.products.add_edit.components.AiScanImageItemUi
import com.example.easymart.presentation.ui.admin.products.add_edit.components.AiScanningBottomSheet
import com.example.easymart.presentation.ui.admin.products.add_edit.components.CategoryPickerSection
import com.example.easymart.presentation.ui.admin.products.add_edit.components.InputBox
import com.example.easymart.presentation.ui.admin.products.add_edit.components.ProductDescriptionInput
import com.example.easymart.presentation.ui.admin.products.add_edit.components.ProductImageGallerySection
import com.example.easymart.presentation.ui.admin.products.add_edit.components.StockQuantityStepper
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAddEditProductScreen(
    uiState: AdminAddEditProductUiState,
    onTitleChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onImageSelected: (String) -> Unit,
    onImagesSelected: (List<String>) -> Unit,
    onSelectImage: (Int) -> Unit,
    onDeleteImage: (Int) -> Unit,
    onDeleteMainImage: () -> Unit,
    onScanImageSelected: (String) -> Unit,
    onSave: () -> Unit,
    onNavigateBack: () -> Unit,
    onQuantityChange: (String) -> Unit,
    onScanWithAi: () -> Unit,
    onRetryScan: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val dimens = LocalAppDimens.current
    val context = LocalContext.current
    var showAiPickerSheet by remember { mutableStateOf(false) }
    var showOcrText by remember { mutableStateOf(false) }
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }
    var showMainImagePreview by remember { mutableStateOf(false) }

    val isBusy = uiState.isLoading || uiState.isScanning
    val aiHighlightColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.35f)
    val quantityValue = uiState.quantity.toIntOrNull() ?: 0

    // lấy painter cho ảnh chính
    val mainImagePainter = if (uiState.mainImageUri.isNotBlank()) {
        rememberAsyncImagePainter(uiState.mainImageUri.trim())
    } else {
        null
    }
    //val mainImageLoading = mainImagePainter?.state is AsyncImagePainter.State.Loading

    // lấy danh sách ảnh phụ (imageUris đã là ảnh phụ)
    val thumbnailPainters = uiState.imageUris
        .map { rememberAsyncImagePainter(it) }

    // map index thực của ảnh phụ
    val thumbnails = uiState.imageUris
        .mapIndexed { idx, uri -> idx to uri }

    // Chuẩn hóa dữ liệu ảnh cho AI picker sheet (bao gồm ảnh chính)
    val aiImageItems = buildList {
        if (uiState.mainImageUri.isNotBlank()) {
            add(
                AiScanImageItemUi(
                    id = uiState.mainImageUri,
                    title = "Ảnh chính",
                    subtitle = "Ảnh",
                    painter = rememberAsyncImagePainter(uiState.mainImageUri)
                )
            )
        }
        uiState.imageUris.forEach { uri ->
            add(
                AiScanImageItemUi(
                    id = uri,
                    title = "Ảnh phụ",
                    subtitle = "Ảnh",
                    painter = rememberAsyncImagePainter(uri)
                )
            )
        }
    }

    // Khởi tạo launcher cho chụp ảnh bằng camera
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                onImageSelected(pendingCameraUri?.toString().orEmpty())
            }
        }
    )

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            onImageSelected(uri?.toString().orEmpty())
        }
    )

    // Launcher để chọn nhiều ảnh từ thư viện
    val multiImagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris ->
            onImagesSelected(uris.map { it.toString() })
        }
    )

    //  bottom sheet để chọn ảnh quét AI
    AiImagePickerBottomSheet(
        visible = showAiPickerSheet,
        items = aiImageItems,
        selectedImageId = uiState.scanImageUri ?: uiState.mainImageUri,
        onSelectImage = { id -> onScanImageSelected(id) },
        onDismiss = { showAiPickerSheet = false },
        onStartScan = {
            showAiPickerSheet = false
            onScanWithAi()
        }
    )

    // bottom sheet hiển thị đang quét AI
    AiScanningBottomSheet(
        visible = uiState.isScanning,
        selectedImagePainter = uiState.scanImageUri?.let { rememberAsyncImagePainter(it) },
        onCancelClick = onRetryScan
    )

    // preview ảnh chính khi click vào ảnh chính
    if (showMainImagePreview && uiState.mainImageUri.isNotBlank()) {
        Dialog(
            onDismissRequest = { showMainImagePreview = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            val previewPainter = rememberAsyncImagePainter(uiState.mainImageUri)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.86f))
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Xem ảnh sản phẩm",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )

                        TextButton(
                            onClick = { showMainImagePreview = false }
                        ) {
                            Text(
                                text = "Đóng",
                                color = Color.White
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = previewPainter,
                            contentDescription = "Ảnh sản phẩm",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        )

                        if (previewPainter.state is AsyncImagePainter.State.Loading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.18f)),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }

    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = dimens.screenPadding),
                verticalArrangement = Arrangement.spacedBy(dimens.spaceMd)
            ) {
                Spacer(modifier = Modifier.height(dimens.spaceSm))

                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(dimens.radiusLarge),
                    elevation = CardDefaults.cardElevation(dimens.cardElevation)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimens.spaceMd),
                        verticalArrangement = Arrangement.spacedBy(dimens.spaceSm)
                    ) {
                        Text(
                            text = "1. Thông tin sản phẩm",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(dimens.spaceSm)) {
                            Text(
                                text = "Tiêu đề sản phẩm *",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Medium
                            )
                            InputBox(
                                value = uiState.title,
                                onValueChange = onTitleChange,
                                hint = "Nhập tên sản phẩm",
                                leadingIcon = Icons.Rounded.Tag,
                                maxLength = 100,
                                containerColor = if (uiState.aiFilledTitle) aiHighlightColor else MaterialTheme.colorScheme.surface,
                                borderColor = if (uiState.titleError != null) MaterialTheme.colorScheme.error else Color.Unspecified,
                                focusBorderColor = if (uiState.titleError != null) MaterialTheme.colorScheme.error else Color.Unspecified
                            )
                            if (uiState.titleError != null) {
                                Text(
                                    text = uiState.titleError,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                            )
                            }

                            Text(
                                text = "Giá bán *",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Medium
                            )
                            InputBox(
                                value = uiState.price,
                                onValueChange = onPriceChange,
                                hint = "Nhập giá sản phẩm",
                                leadingIcon = Icons.Rounded.AttachMoney,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                maxLength = 12,
                                showCounter = false,
                                containerColor = MaterialTheme.colorScheme.surface,
                                borderColor = if (uiState.priceError != null) MaterialTheme.colorScheme.error else Color.Unspecified,
                                focusBorderColor = if (uiState.priceError != null) MaterialTheme.colorScheme.error else Color.Unspecified
                            )
                            if (uiState.priceError != null) {
                                Text(
                                    text = uiState.priceError,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                            )
                            }

                            Text(
                                text = "Số lượng tồn kho *",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Row (
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ){
                                StockQuantityStepper(
                                    quantity = quantityValue,
                                    onIncrease = { onQuantityChange((quantityValue + 1).toString()) },
                                    onDecrease = {
                                        val nextValue = (quantityValue - 1).coerceAtLeast(0)
                                        onQuantityChange(nextValue.toString())
                                    },
                                    onQuantityChange = { onQuantityChange(it.toString()) },
                                )
                            }
                            if (uiState.quantityError != null) {
                                Text(
                                    text = uiState.quantityError,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                            )
                            }

                            CategoryPickerSection(
                                value = uiState.category,
                                onValueChange = onCategoryChange,
                                categories = uiState.categories
                            )
                            if (uiState.categoryError != null) {
                                Text(
                                    text = uiState.categoryError,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                            )
                            }

                            Text(
                                text = "Mô tả sản phẩm *",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(dimens.radiusLarge))
                                    .background(
                                        if (uiState.aiFilledDescription) aiHighlightColor else Color.Transparent
                                    )
                                    .padding(2.dp)
                            ) {
                                ProductDescriptionInput(
                                    text = uiState.description,
                                    onTextChange = onDescriptionChange,
                                    modifier = Modifier.fillMaxWidth()
                            )
                            }
                            if (uiState.descriptionError != null) {
                                Text(
                                    text = uiState.descriptionError,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                            )
                            }
                        }
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(dimens.radiusLarge),
                    elevation = CardDefaults.cardElevation(dimens.cardElevation)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimens.spaceMd),
                        verticalArrangement = Arrangement.spacedBy(dimens.spaceSm)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "2. Hình ảnh sản phẩm",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            AiScanStatusBadge(
                                isScanning = uiState.isScanning,
                                isSuccess = uiState.scanSuccess,
                                isError = uiState.scanError != null
                            )
                        }

                        //Box(modifier = Modifier.fillMaxWidth()) {
                            ProductImageGallerySection(
                                mainImagePainter = mainImagePainter,
                                thumbnailPainters = thumbnailPainters,
                                onAddImageClick = { multiImagePicker.launch("image/*") },
                                onCaptureImageClick = {
                                    val uri = createImageUri(context)
                                    pendingCameraUri = uri
                                    takePictureLauncher.launch(uri)
                                },
                                onDeleteMainImageClick = onDeleteMainImage,
                                onThumbnailClick = { index -> onSelectImage(index) },
                                onThumbnailDeleteClick = { index -> onDeleteImage(index) },
                                selectedThumbnailIndex = -1,
                                aiScanEnabled = !isBusy && uiState.mainImageUri.isNotBlank(),
                                aiScanOnClick = { showAiPickerSheet = true },
                                onShowImageClick = {
                                    if (uiState.mainImageUri.isNotBlank()) {
                                        showMainImagePreview = true
                                    }
                                }
                            )

//                            if (uiState.mainImageUri.isNotBlank()) {
//                                Box(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .clickable { showMainImagePreview = true }
//                                )
//                            }

//                            if (mainImageLoading) {
//                                Box(
//                                    modifier = Modifier
//                                        .matchParentSize()
//                                        .background(Color.Black.copy(alpha = 0.15f)),
//                                    contentAlignment = Alignment.Center
//                                ) {
//                                    CircularProgressIndicator()
//                                }
//                            }
                        //}

                        if (uiState.imageUriError != null) {
                            Text(
                                text = uiState.imageUriError,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                if (uiState.scanSuccess) {
                    AiScanSuccessCard(
                        onScanOtherClick = { showAiPickerSheet = true },
                        onEditClick = {}
                    )
                }

                if (uiState.scanError != null) {
                    AiScanErrorCard(
                        onChooseAnotherClick = { showAiPickerSheet = true }
                    )
                }

                val hasSuggestions = listOf(
                    uiState.suggestedTitle,
                    uiState.suggestedCategory,
                    uiState.suggestedDescription
                ).any { !it.isNullOrBlank() }

                if (uiState.scanSuccess || hasSuggestions) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(dimens.radiusLarge),
                        elevation = CardDefaults.cardElevation(dimens.cardElevation)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(dimens.spaceMd),
                            verticalArrangement = Arrangement.spacedBy(dimens.spaceSm)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm)
                            ) {
                                Text(
                                    text = "AI Suggestions",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(MaterialTheme.colorScheme.secondaryContainer)
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "AI Suggestion",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }

                            SuggestionRow("Tên gợi ý", uiState.suggestedTitle)
                            SuggestionRow("Danh mục gợi ý", uiState.suggestedCategory)
                            SuggestionRow("Mô tả gợi ý", uiState.suggestedDescription)

                            if (uiState.suggestionConfidence != null) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(
                                        text = "Độ tin cậy: ${(uiState.suggestionConfidence * 100).toInt()}%",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    LinearProgressIndicator(
                                        progress = { uiState.suggestionConfidence.coerceIn(0f, 1f) },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(dimens.radiusLarge),
                    elevation = CardDefaults.cardElevation(dimens.cardElevation)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimens.spaceMd),
                        verticalArrangement = Arrangement.spacedBy(dimens.spaceSm)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "OCR raw text",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            TextButton(onClick = { showOcrText = !showOcrText }) {
                                Text(if (showOcrText) "Ẩn" else "Xem")
                            }
                        }

                        AnimatedVisibility(visible = showOcrText) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (uiState.ocrText.isNotBlank()) {
                                    Text(
                                        text = uiState.ocrText,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                } else {
                                    Text(
                                        text = "Chưa có dữ liệu OCR.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                if (uiState.error != null) {
                    Text(text = uiState.error, color = MaterialTheme.colorScheme.error)
                }

                Button(
                    onClick = onSave,
                    enabled = !isBusy,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF57C00))
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            color = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                    }
                    Icon(Icons.Filled.Save, contentDescription = null)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Lưu sản phẩm")
                }

                Spacer(modifier = Modifier.height(dimens.spaceMd))
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = dimens.spaceMd)
            )
        }
    }
}

@Composable
private fun AiScanStatusBadge(
    isScanning: Boolean,
    isSuccess: Boolean,
    isError: Boolean
) {
    val (label, container, content) = when {
        isScanning -> Triple(
            "Đang quét",
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer
        )
        isError -> Triple(
            "Lỗi",
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.onErrorContainer
        )
        isSuccess -> Triple(
            "Hoàn tất",
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer
        )
        else -> Triple(
            "Chưa quét",
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(container)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = content)
    }
}

@Composable
private fun SuggestionRow(label: String, value: String?) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value?.takeIf { it.isNotBlank() } ?: "Chưa có gợi ý",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview
@Composable
fun AdminAddEditProductScreenPreview() {
    EasyMartTheme {
        AdminAddEditProductScreen(
            uiState = AdminAddEditProductUiState(
                title = "Áo thun nam cổ tròn",
                price = "199000",
                description = "Áo thun nam cổ tròn chất liệu cotton mềm mại, thoáng mát, phù hợp mặc hàng ngày. Thiết kế đơn giản dễ phối đồ.",
                category = "Thời trang nam",
                categories = listOf("Thời trang nam", "Thời trang nữ", "Điện tử", "Gia dụng"),
                quantity = "50",
                mainImageUri = "",
                imageUris = listOf(
                ),
                isLoading = false,
                isScanning = false,
                scanSuccess = true,
                suggestedTitle = "Áo thun nam cổ tròn - Gợi ý AI",
                suggestedCategory = "Thời trang nam - Gợi ý AI",
                suggestedDescription = "Áo thun nam cổ tròn chất liệu cotton, thiết kế đơn giản, phù hợp mặc hàng ngày. (Gợi ý từ AI)"
            ),

            onTitleChange = {},
            onPriceChange = {},
            onDescriptionChange = {},
            onCategoryChange = {},
            onImageSelected = {},
            onImagesSelected = {},
            onSelectImage = {},
            onDeleteImage = {},
            onDeleteMainImage = {},
            onScanImageSelected = {},
            onSave = {},
            onQuantityChange = {},
            onScanWithAi = {},
            onRetryScan = {},
            snackbarHostState = remember { SnackbarHostState() },
            onNavigateBack = {}
        )
    }
}

private fun createImageUri(context: Context): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val imageDir = File(context.cacheDir, "images").apply { mkdirs() }
    val imageFile = File(imageDir, "IMG_${timeStamp}.jpg")
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        imageFile
    )
}


@Preview
@Composable
fun TestPreview(){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(460.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter("https://firebasestorage.googleapis.com/v0/b/atomic-vault-448115-r9.firebasestorage.app/o/products%2F2115763499%2Fmain.jpg?alt=media&token=1e082b56-5c1d-434e-9d00-ed3d52d7c5e4"),
                contentDescription = "Ảnh sản phẩm",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .clip(RoundedCornerShape(20.dp))
            )


        }
    }
}


