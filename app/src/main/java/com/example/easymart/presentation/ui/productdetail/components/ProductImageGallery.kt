package com.example.easymart.presentation.ui.productdetail.components
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.colors.LocalAppColors
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.mock.mockProducts
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ProductImageGallery(
    product: Product,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    val imageSources = product.detailImageSources().ifEmpty { listOf("") }

    val pagerState = rememberPagerState(
        pageCount = { imageSources.size }
    )

    val scope = rememberCoroutineScope()

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(dimens.radiusXl),
        color = appColors.surfaceContainer,
        shadowElevation = dimens.cardElevation,
        border = BorderStroke(
            width = dimens.dividerThickness,
            color = appColors.outlineVariant
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(appColors.surfaceContainerHigh)
                .padding(dimens.spaceMd)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                pageSpacing = dimens.spaceMd
            ) { page ->
                ProductGalleryImagePage(
                    imageUrl = imageSources[page],
                    imageRes = product.imageRes,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(dimens.radiusLarge)
                )
            }

            if (imageSources.size > 1) {
                ProductImageCounter(
                    currentPage = pagerState.currentPage + 1,
                    totalPage = imageSources.size,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(dimens.spaceSm)
                )

//                ProductThumbnailRail(
//                    images = imageSources,
//                    selectedIndex = pagerState.currentPage,
//                    productImageRes = product.imageRes,
//                    productName = product.name,
//                    onImageSelected = { index ->
//                        scope.launch {
//                            pagerState.animateScrollToPage(index)
//                        }
//                    },
//                    modifier = Modifier
//                        .align(Alignment.CenterEnd)
//                        .padding(end = dimens.spaceXs)
//                )

                ProductImageDotsIndicator(
                    pageCount = imageSources.size,
                    selectedIndex = pagerState.currentPage,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = dimens.spaceSm)
                )
            }
        }
    }
}

// hỗ trợ gộp tất cả các nguồn ảnh thành 1 danh sách duy nhất
private fun Product.detailImageSources(): List<String> {
    return buildList {
        //add(localImageUri.orEmpty())
        add(imageUrl)
        //addAll(localImageUris)
        addAll(imageUrls)
    }
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .distinct() // loại bỏ trùng
}

@Composable
private fun ProductGalleryImagePage(
    imageUrl: String,
    imageRes: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
    shape: Shape
) {
    val isPreview = LocalInspectionMode.current

    if (isPreview && imageUrl.isBlank() && imageRes != 0) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = contentDescription,
            modifier = modifier
                .clip(shape)
                .background(MaterialTheme.colorScheme.surface),
            contentScale = ContentScale.Fit
        )
    } else {
        ProductImageBox(
            imageUrl = imageUrl,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Fit,
            shape = shape,
            showLoadingIndicator = true,
            showFallbackText = true
        )
    }
}

@Composable
private fun ProductImageCounter(
    currentPage: Int,
    totalPage: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(999.dp),
        color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.48f),
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Text(
            text = stringResource(R.string.ui, currentPage, totalPage),
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 5.dp
            ),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}


@Composable
private fun ProductThumbnailRail(
    images: List<String>,
    selectedIndex: Int,
    productImageRes: Int,
    productName: String,
    onImageSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    Surface(
        modifier = modifier
            .width(58.dp)
            .heightIn(max = 280.dp),
        shape = RoundedCornerShape(999.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.82f),
        shadowElevation = 2.dp
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = dimens.spaceSm),
            verticalArrangement = Arrangement.spacedBy(dimens.spaceSm),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = dimens.spaceXs)
        ) {
            itemsIndexed(images) { index, imageUrl ->
                ProductThumbnailItem(
                    imageUrl = imageUrl,
                    imageRes = productImageRes,
                    contentDescription = productName,
                    selected = index == selectedIndex,
                    onClick = { onImageSelected(index) }
                )
            }
        }
    }
}

@Composable
private fun ProductThumbnailItem(
    imageUrl: String,
    imageRes: Int,
    contentDescription: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val appColors = LocalAppColors.current
    val shape = RoundedCornerShape(14.dp)

    Surface(
        onClick = onClick,
        modifier = Modifier
            .size(44.dp)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    appColors.outlineVariant
                },
                shape = shape
            ),
        shape = shape,
        color = appColors.surfaceContainer
    ) {
        ProductThumbnailImage(
            imageUrl = imageUrl,
            imageRes = imageRes,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            shape = shape
        )
    }
}

@Composable
private fun ProductThumbnailImage(
    imageUrl: String,
    imageRes: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
    shape: Shape
) {
    val isPreview = LocalInspectionMode.current

    if (isPreview && imageUrl.isBlank() && imageRes != 0) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = contentDescription,
            modifier = modifier
                .clip(shape)
                .background(MaterialTheme.colorScheme.surface),
            contentScale = ContentScale.Crop
        )
    } else {
        ProductImageBox(
            imageUrl = imageUrl,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Crop,
            shape = shape,
            showLoadingIndicator = false,
            showFallbackText = false
        )
    }
}

@Composable
private fun ProductImageDotsIndicator(
    pageCount: Int,
    selectedIndex: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val selected = index == selectedIndex

            Box(
                modifier = Modifier
                    .width(if (selected) 18.dp else 6.dp)
                    .size(height = 6.dp, width = if (selected) 18.dp else 6.dp)
                    .clip(CircleShape)
                    .background(
                        if (selected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)
                        }
                    )
            )
        }
    }
}

@Preview(
    name = "Gallery - Single Image",
    showBackground = true
)
@Composable
private fun ProductImageGallerySingleImagePreview() {
    EasyMartTheme {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            ProductImageGallery(
                product = mockProducts.first().copy(
                    imageUrl = "",
                    imageUrls = emptyList()
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
        }
    }
}

@Preview(
    name = "Gallery - Multiple Images",
    showBackground = true
)
@Composable
private fun ProductImageGalleryMultipleImagesPreview() {
    EasyMartTheme {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            ProductImageGallery(
                product = mockProducts.first().copy(
                    imageUrl = "",
                    imageUrls = listOf(
                        "",
                        "preview-image-1",
                        "preview-image-2",
                        "preview-image-3",
                        "preview-image-4"
                    )
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
        }
    }
}

@Preview(
    name = "Image Counter",
    showBackground = true
)
@Composable
private fun ProductImageCounterPreview() {
    EasyMartTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp)
        ) {
            ProductImageCounter(
                currentPage = 2,
                totalPage = 5
            )
        }
    }
}

@Preview(
    name = "Thumbnail Rail",
    showBackground = true
)
@Composable
private fun ProductThumbnailRailPreview() {
    val product = mockProducts.first()

    EasyMartTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
        ) {
            ProductThumbnailRail(
                images = listOf(
                    "",
                    "preview-image-1",
                    "preview-image-2",
                    "preview-image-3",
                    "preview-image-4"
                ),
                selectedIndex = 1,
                productImageRes = product.imageRes,
                productName = product.name,
                onImageSelected = {},
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview(
    name = "Thumbnail Item - Selected",
    showBackground = true
)
@Composable
private fun ProductThumbnailItemSelectedPreview() {
    val product = mockProducts.first()

    EasyMartTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
        ) {
            ProductThumbnailItem(
                imageUrl = "",
                imageRes = product.imageRes,
                contentDescription = product.name,
                selected = true,
                onClick = {}
            )
        }
    }
}


@Preview(
    name = "Thumbnail Items - States",
    showBackground = true
)
@Composable
private fun ProductThumbnailItemsStatesPreview() {
    val product = mockProducts.first()

    EasyMartTheme {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductThumbnailItem(
                imageUrl = "",
                imageRes = product.imageRes,
                contentDescription = product.name,
                selected = true,
                onClick = {}
            )

            ProductThumbnailItem(
                imageUrl = "preview-image-1",
                imageRes = product.imageRes,
                contentDescription = product.name,
                selected = false,
                onClick = {}
            )

            ProductThumbnailItem(
                imageUrl = "preview-image-2",
                imageRes = product.imageRes,
                contentDescription = product.name,
                selected = false,
                onClick = {}
            )
        }
    }
}

@Preview(
    name = "Dots Indicator - First Page",
    showBackground = true
)
@Composable
private fun ProductImageDotsIndicatorFirstPreview() {
    EasyMartTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
        ) {
            ProductImageDotsIndicator(
                pageCount = 5,
                selectedIndex = 0
            )
        }
    }
}

@Preview(
    name = "Dots Indicator - Middle Page",
    showBackground = true
)
@Composable
private fun ProductImageDotsIndicatorMiddlePreview() {
    EasyMartTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
        ) {
            ProductImageDotsIndicator(
                pageCount = 5,
                selectedIndex = 2
            )
        }
    }
}

@Preview(
    name = "Dots Indicator - Last Page",
    showBackground = true
)
@Composable
private fun ProductImageDotsIndicatorLastPreview() {
    EasyMartTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
        ) {
            ProductImageDotsIndicator(
                pageCount = 5,
                selectedIndex = 4
            )
        }
    }
}

@Preview(
    name = "Image Page - Local Preview Resource",
    showBackground = true
)
@Composable
private fun ProductGalleryImagePagePreview() {
    val product = mockProducts.first()

    EasyMartTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .size(260.dp)
        ) {
            ProductGalleryImagePage(
                imageUrl = "",
                imageRes = product.imageRes,
                contentDescription = product.name,
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(24.dp)
            )
        }
    }
}

@Preview(
    name = "Thumbnail Image - Local Preview Resource",
    showBackground = true
)
@Composable
private fun ProductThumbnailImagePreview() {
    val product = mockProducts.first()

    EasyMartTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
                .size(72.dp)
        ) {
            ProductThumbnailImage(
                imageUrl = "",
                imageRes = product.imageRes,
                contentDescription = product.name,
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@Preview(
    name = "Gallery Components Overview",
    showBackground = true,
    device = Devices.PIXEL_4
)
@Composable
private fun ProductImageGalleryComponentsOverviewPreview() {
    val product = mockProducts.first()

    EasyMartTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ProductImageCounter(
                currentPage = 3,
                totalPage = 6
            )

            ProductImageDotsIndicator(
                pageCount = 6,
                selectedIndex = 2
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProductThumbnailItem(
                    imageUrl = "",
                    imageRes = product.imageRes,
                    contentDescription = product.name,
                    selected = true,
                    onClick = {}
                )

                ProductThumbnailItem(
                    imageUrl = "preview-image-1",
                    imageRes = product.imageRes,
                    contentDescription = product.name,
                    selected = false,
                    onClick = {}
                )
            }

            ProductThumbnailRail(
                images = listOf(
                    "",
                    "preview-image-1",
                    "preview-image-2",
                    "preview-image-3"
                ),
                selectedIndex = 0,
                productImageRes = product.imageRes,
                productName = product.name,
                onImageSelected = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductImageGalleryPreview() {
    EasyMartTheme {
        ProductImageGallery(
            product = mockProducts.first(),
            modifier = Modifier
                .width(360.dp)
                .aspectRatio(1f)
        )
    }
}
