package com.example.easymart.presentation.ui.image_search.components

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ImageSearch
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.colors.LocalAppColors
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun ImageSearchPickerCard(
    selectedImage: Uri?,
    loading: Boolean,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onClearClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current
    val canSearch = selectedImage != null && !loading

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.radiusLarge),
        color = appColors.surfaceContainer,
        border = BorderStroke(dimens.dividerThickness, appColors.aiAccent.copy(alpha = 0.22f))
    ) {
        Column(
            modifier = Modifier.padding(dimens.spaceMd),
            verticalArrangement = Arrangement.spacedBy(dimens.spaceMd)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm)
            ) {
                OutlinedButton(
                    onClick = onCameraClick,
                    enabled = !loading,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(dimens.radiusMedium),
                    contentPadding = PaddingValues(vertical = dimens.spaceSm),
                    border = BorderStroke(dimens.dividerThickness, appColors.aiAccent.copy(alpha = 0.36f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = appColors.aiAccent
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PhotoCamera,
                        contentDescription = null,
                        modifier = Modifier.size(dimens.iconSmall)
                    )
                    Text(
                        text = "Camera",
                        modifier = Modifier.padding(start = dimens.spaceXs)
                    )
                }

                OutlinedButton(
                    onClick = onGalleryClick,
                    enabled = !loading,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(dimens.radiusMedium),
                    contentPadding = PaddingValues(vertical = dimens.spaceSm),
                    border = BorderStroke(dimens.dividerThickness, appColors.aiAccent.copy(alpha = 0.36f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = appColors.aiAccent
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PhotoLibrary,
                        contentDescription = null,
                        modifier = Modifier.size(dimens.iconSmall)
                    )
                    Text(
                        text = "Gallery",
                        modifier = Modifier.padding(start = dimens.spaceXs)
                    )
                }
            }

            ImagePreviewPanel(
                selectedImage = selectedImage,
                loading = loading,
                onClearClick = onClearClick
            )

            Button(
                onClick = onSearchClick,
                enabled = canSearch,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(dimens.radiusMedium),
                contentPadding = PaddingValues(vertical = dimens.spaceMd),
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = appColors.disabledContainer,
                    disabledContentColor = appColors.disabledContent
                )
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(dimens.iconSmall),
                        strokeWidth = dimens.dividerThickness,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.ImageSearch,
                        contentDescription = null,
                        modifier = Modifier.size(dimens.iconSmall)
                    )
                }
                Text(
                    text = if (selectedImage == null) "Chọn ảnh để tìm kiếm" else "Tìm kiếm",
                    modifier = Modifier.padding(start = dimens.spaceXs),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
private fun ImagePreviewPanel(
    selectedImage: Uri?,
    loading: Boolean,
    onClearClick: () -> Unit
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimens.productImageHeight)
            .clip(RoundedCornerShape(dimens.radiusLarge))
            .background(appColors.aiAccentLight)
            .border(
                BorderStroke(dimens.dividerThickness, appColors.aiAccent.copy(alpha = 0.28f)),
                RoundedCornerShape(dimens.radiusLarge)
            ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = selectedImage,
            label = "image_search_preview"
        ) { imageUri ->
            if (imageUri == null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimens.spaceSm)
                ) {
                    Surface(
                        shape = RoundedCornerShape(dimens.radiusXl),
                        color = appColors.aiAccentSoft,
                        contentColor = appColors.aiAccent,
                        border = BorderStroke(
                            dimens.dividerThickness,
                            appColors.aiAccent.copy(alpha = 0.32f)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ImageSearch,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(dimens.spaceMd)
                                .size(dimens.iconLarge)
                        )
                    }
                    Text(
                        text = "Chưa chọn ảnh",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = appColors.aiAccent
                    )
                }
            } else {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Ảnh tìm kiếm",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = onClearClick,
                    enabled = !loading,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(dimens.spaceXs)
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                            shape = RoundedCornerShape(dimens.radiusMedium)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Xóa ảnh",
                        tint = appColors.textPrimary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ImageSearchPickerCardPreview() {
    EasyMartTheme {
        ImageSearchPickerCard(
            selectedImage = null,
            loading = false,
            onCameraClick = {},
            onGalleryClick = {},
            onClearClick = {},
            onSearchClick = {}
        )
    }
}
