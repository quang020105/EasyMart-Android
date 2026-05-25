package com.example.easymart.presentation.ui.admin.products.add_edit.components

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.presentation.theme.EasyMartTheme

@Composable
fun ProductImageGallerySection(
    mainImagePainter: Painter?,
    thumbnailPainters: List<Painter>,
    onAddImageClick: () -> Unit,
    onCaptureImageClick: () -> Unit,
    onDeleteMainImageClick: () -> Unit,
    onThumbnailClick: (Int) -> Unit,
    onThumbnailDeleteClick: (Int) -> Unit,
    selectedThumbnailIndex: Int = 0,
    aiScanEnabled: Boolean = true,
    aiScanOnClick: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Image,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Hình ảnh sản phẩm",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (mainImagePainter == null) {
                EmptyProductImagesPlaceholder(
                    onAddImagesClick = onAddImageClick
                )
            } else {
                ProductMainImageCard(
                    painter = mainImagePainter,
                    onDeleteClick = onDeleteMainImageClick
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (thumbnailPainters.isEmpty()) {
                    "Chưa có ảnh phụ nào"
                } else {
                    "Ảnh khác (${thumbnailPainters.size}/10)"
                },
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                thumbnailPainters.forEachIndexed { index, painter ->
                    ProductImageThumbnail(
                        painter = painter,
                        selected = index == selectedThumbnailIndex,
                        onClick = { onThumbnailClick(index) },
                        onDeleteClick = { onThumbnailDeleteClick(index) }
                    )
                }

                AddImageTile(
                    onClick = onAddImageClick
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ImageActionButton(
                    title = "Thêm ảnh",
                    icon = Icons.Rounded.PhotoLibrary,
                    iconTint = MaterialTheme.colorScheme.primary,
                    onClick = onAddImageClick,
                    modifier = Modifier.weight(1f)
                )

                ImageActionButton(
                    title = "Chụp ảnh",
                    icon = Icons.Rounded.PhotoCamera,
                    iconTint = Color(0xFF2E7D32),
                    onClick = onCaptureImageClick,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            AiProductScanCard(
                onScanClick = aiScanOnClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = aiScanEnabled
            )
        }
    }
}

@Composable
fun ProductMainImageCard(
    painter: Painter,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        )

        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp),
            shape = RoundedCornerShape(999.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.92f)
        ) {
            Text(
                text = "Ảnh chính",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .size(28.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
                .clickable(onClick = onDeleteClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "Xóa ảnh chính",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
        }

        Text(
            text = "Kéo để thay đổi ảnh chính",
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 10.dp)
        )
    }
}

@Composable
fun ProductImageThumbnail(
    painter: Painter,
    selected: Boolean,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }

    Box(
        modifier = modifier
            .size(70.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                BorderStroke(
                    width = if (selected) 2.dp else 1.dp,
                    color = borderColor
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(70.dp)
        )

        if (selected) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .padding(2.dp)
                        .size(12.dp)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                    .clickable(onClick = onDeleteClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Xóa ảnh",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
fun AddImageTile(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(70.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                shape = RoundedCornerShape(16.dp)
            )
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "Thêm ảnh",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp)
        )
    }
}




@Preview(showBackground = true)
@Composable
private fun ProductImageGallerySectionPreview() {
    EasyMartTheme {
        ProductImageGallerySection(
            mainImagePainter = ColorPainter(Color(0xFFF2F2F2)),
            thumbnailPainters = listOf(
                ColorPainter(Color(0xFFF2F2F2)),
                ColorPainter(Color(0xFFEAEAEA)),
                ColorPainter(Color(0xFFE0E0E0))
            ),
            selectedThumbnailIndex = 1,
            onAddImageClick = {},
            onCaptureImageClick = {},
            onDeleteMainImageClick = {},
            onThumbnailClick = {},
            onThumbnailDeleteClick = {},
            aiScanOnClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductMainImageCardPreview() {
    EasyMartTheme {
        ProductMainImageCard(
            painter = ColorPainter(Color(0xFFF1F1F1)),
            onDeleteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductImageThumbnailPreview() {
    EasyMartTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProductImageThumbnail(
                painter = ColorPainter(Color(0xFFF1F1F1)),
                selected = false,
                onClick = {},
                onDeleteClick = {}
            )
            ProductImageThumbnail(
                painter = ColorPainter(Color(0xFFE5E5E5)),
                selected = true,
                onClick = {},
                onDeleteClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddImageTilePreview() {
    EasyMartTheme {
        AddImageTile(onClick = {})
    }
}
