package com.example.easymart.presentation.ui.admin.products.detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.ui.mock.mockSimpleProduct
import androidx.compose.ui.window.Dialog

@Composable
fun ProductOverviewCard(
    product: Product,
    modifier: Modifier = Modifier
) {
    val images = remember(product) {
        buildList {
            if (product.imageUrl.isNotBlank()) add(product.imageUrl)
            addAll(product.imageUrls.filter { it.isNotBlank() })
            if (!product.localImageUri.isNullOrBlank()) add(product.localImageUri)
            addAll(product.localImageUris.filter { it.isNotBlank() })
        }.distinct()
    }

    var selectedImageIndex by remember {
        mutableIntStateOf(0)
    }

    val selectedImage = images.getOrNull(selectedImageIndex)

    var isPreviewVisible by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFF2F4F7)),
                contentAlignment = Alignment.Center
            ) {
                if (selectedImage != null) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImage),
                        contentDescription = product.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isPreviewVisible = true },
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (images.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    images.forEachIndexed { index, image ->
                        ProductThumbnailItem(
                            imageUrl = image,
                            selected = index == selectedImageIndex,
                            onClick = {
                                selectedImageIndex = index
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    ProductVisibilityBadge(isVisible = product.isVisible)

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = product.category.ifBlank { "Chưa có danh mục" },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = formatProductPrice(product.price),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2563EB)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ProductSourceBadge(product = product)

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB020),
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = product.rating.rate.toString(),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Box(
                            modifier = Modifier
                                .height(22.dp)
                                .width(1.dp)
                                .background(MaterialTheme.colorScheme.outlineVariant)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "${product.rating.count} đánh giá",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    if (isPreviewVisible && selectedImage != null) {
        Dialog(onDismissRequest = { isPreviewVisible = false }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f))
                    .clickable { isPreviewVisible = false }
            ) {
                Image(
                    painter = rememberAsyncImagePainter(selectedImage),
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentScale = ContentScale.Fit
                )

                IconButton(
                    onClick = { isPreviewVisible = false },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Đóng preview",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductThumbnailItem(
    imageUrl: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (selected) {
        Color(0xFF2563EB)
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }

    Box(
        modifier = Modifier
            .size(width = 76.dp, height = 58.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF2F4F7))
            .border(
                border = BorderStroke(
                    width = if (selected) 2.dp else 1.dp,
                    color = borderColor
                ),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Fit
        )
    }
}


@Preview
@Composable
fun ProductOverviewCardPreview() {

    EasyMartTheme {
        ProductOverviewCard(product = mockSimpleProduct)
    }
}
