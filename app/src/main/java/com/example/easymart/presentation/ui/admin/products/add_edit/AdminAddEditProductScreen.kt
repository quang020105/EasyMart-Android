package com.example.easymart.presentation.ui.admin.products.add_edit

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import coil.compose.AsyncImage
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.content.FileProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAddEditProductScreen(
    uiState: AdminAddEditProductUiState,
    onTitleChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onImageSelected: (String) -> Unit,
    onSave: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val dimens = LocalAppDimens.current
    val context = LocalContext.current
    var showSourceSheet by remember { mutableStateOf(false) }
    var showImagePreview by remember { mutableStateOf(false) }
    // lưu URI tạm thời khi chụp ảnh từ camera
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }

    // nhận kết quả chụp ảnh từ camera
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                onImageSelected(pendingCameraUri?.toString().orEmpty())
            }
        }
    )

    // nhận kết quả chọn ảnh từ thư viện
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            onImageSelected(uri?.toString().orEmpty())
        }
    )

    // chọn chụp ảnh hay chọn từ thư viện
//    if (showSourceSheet) {
//        ModalBottomSheet(onDismissRequest = { showSourceSheet = false }) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(dimens.spaceMd),
//                verticalArrangement = Arrangement.spacedBy(dimens.spaceSm)
//            ) {
//                Text(
//                    text = "Chọn nguồn ảnh",
//                    style = MaterialTheme.typography.titleMedium,
//                    fontWeight = FontWeight.SemiBold
//                )
//                TextButton(
//                    onClick = {
//                        showSourceSheet = false
//                        imagePicker.launch("image/*")
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text("Chọn từ thư viện")
//                }
//                TextButton(
//                    onClick = {
//                        showSourceSheet = false
//                        val uri = createImageUri(context)
//                        pendingCameraUri = uri
//                        takePictureLauncher.launch(uri)
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text("Chụp ảnh")
//                }
//                Spacer(modifier = Modifier.height(dimens.spaceSm))
//            }
//        }
//    }



    // chọn chụp ảnh hay chọn từ thư viện
    if (showSourceSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSourceSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //Column(modifier = Modifier.weight(1f)) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "Chọn nguồn ảnh",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
//                        Text(
//                            text = "Chọn ảnh từ thư viện hoặc chụp mới bằng camera",
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant
//                        )
//                    }

                    TextButton(onClick = { showSourceSheet = false }) {
                        Text("Đóng")
                    }
                }

                androidx.compose.material3.HorizontalDivider()

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showSourceSheet = false
                            imagePicker.launch("image/*")
                        },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PhotoLibrary,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Chọn từ thư viện",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Lấy ảnh có sẵn trong máy",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showSourceSheet = false
                            val uri = createImageUri(context)
                            pendingCameraUri = uri
                            takePictureLauncher.launch(uri)
                        },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(MaterialTheme.colorScheme.secondaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Image,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Chụp ảnh",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Mở camera để chụp ảnh mới",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }


    if (showImagePreview && uiState.imageUri.isNotBlank()) {
        Dialog(onDismissRequest = { showImagePreview = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp, max = 600.dp)
                    .clip(RoundedCornerShape(dimens.radiusLarge))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                AsyncImage(
                    model = uiState.imageUri.trim(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }

    Surface {
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
                    verticalArrangement = Arrangement.spacedBy(dimens.spaceMd)
                ) {
                    OutlinedTextField(
                        value = uiState.title,
                        onValueChange = onTitleChange,
                        label = { Text("Tiêu đề") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = uiState.titleError != null,
                        supportingText = uiState.titleError?.let { { Text(it) } }
                    )

                    OutlinedTextField(
                        value = uiState.price,
                        onValueChange = onPriceChange,
                        label = { Text("Giá") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = uiState.priceError != null,
                        supportingText = uiState.priceError?.let { { Text(it) } }
                    )

                    OutlinedTextField(
                        value = uiState.category,
                        onValueChange = onCategoryChange,
                        label = { Text("Danh mục") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = uiState.categoryError != null,
                        supportingText = uiState.categoryError?.let { { Text(it) } }
                    )

                    OutlinedTextField(
                        value = uiState.description,
                        onValueChange = onDescriptionChange,
                        label = { Text("Mô tả") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4,
                        isError = uiState.descriptionError != null,
                        supportingText = uiState.descriptionError?.let { { Text(it) } }
                    )

                    Button(
                        onClick = { showSourceSheet = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Filled.PhotoLibrary, contentDescription = null)
                        Spacer(modifier = Modifier.size(8.dp))
                        Text("Chọn ảnh")
                    }

                    if (uiState.imageUriError != null) {
                        Text(
                            text = uiState.imageUriError ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
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
                    Text(
                        text = "Preview ảnh",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(dimens.radiusMedium))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable(enabled = uiState.imageUri.isNotBlank()) {
                                showImagePreview = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.imageUri.isNotBlank()) {
                            AsyncImage(
                                model = uiState.imageUri.trim(),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Filled.Image,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    "Ảnh sẽ hiển thị ở đây",
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
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
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
    }
}


@Preview
@Composable
fun AdminAddEditProductScreenPreview() {
    EasyMartTheme {
        AdminAddEditProductScreen(
            uiState = AdminAddEditProductUiState(
                title = "Sản phẩm mẫu",
                price = "99.99",
                description = "Đây là mô tả của sản phẩm mẫu.",
                category = "Giày dép",
                imageUri = "",
                isLoading = false,
                error = null
            ),
            onTitleChange = {},
            onPriceChange = {},
            onDescriptionChange = {},
            onCategoryChange = {},
            onImageSelected = {},
            onSave = {},
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
