package com.example.easymart.presentation.ui.productdetail.components
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.colors.LocalAppColors
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
internal fun ProductImageBox(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    shape: Shape? = null,
    showLoadingIndicator: Boolean = false,
    showFallbackText: Boolean = false
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current
    val imageShape = shape ?: RoundedCornerShape(dimens.radiusLarge)

    if (imageUrl.isNullOrBlank()) {
        ProductImageFallback(
            modifier = modifier
                .clip(imageShape)
                .background(appColors.surfaceContainerHigh),
            showText = showFallbackText
        )
        return
    }

    // SubcomposeAsyncImage: quản lý các trạng thái tải ảnh: loading, error, success
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build(),
        contentDescription = contentDescription,
        modifier = modifier
            .clip(imageShape)
            .background(appColors.surfaceContainer),
        contentScale = contentScale,
        loading = {
            ProductImageLoadingPlaceholder(
                modifier = Modifier.fillMaxSize(),
                showIndicator = showLoadingIndicator
            )
        },
        error = {
            ProductImageFallback(
                modifier = Modifier.fillMaxSize(),
                showText = showFallbackText
            )
        },
        success = {
            SubcomposeAsyncImageContent()
        }
    )
}

@Composable
private fun ProductImageLoadingPlaceholder(
    modifier: Modifier = Modifier,
    showIndicator: Boolean = false
) {
    val appColors = LocalAppColors.current

    val transition = rememberInfiniteTransition(label = "product_image_shimmer")

    val translateAnim by transition.animateFloat(
        initialValue = -600f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1100,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "product_image_shimmer_translate"
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            appColors.surfaceContainer,
            appColors.surfaceContainerHigh,
            appColors.surfaceContainer
        ),
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + 500f, 500f)
    )

    Box(
        modifier = modifier.background(shimmerBrush),
        contentAlignment = Alignment.Center
    ) {
        if (showIndicator) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ProductImageFallback(
    modifier: Modifier = Modifier,
    showText: Boolean = false
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(appColors.surfaceContainerHigh),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Category,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
                )
            }

            if (showText) {
                Text(
                    text = stringResource(R.string.ui_text_324),
                    modifier = Modifier.padding(
                        top = dimens.spaceSm,
                        start = dimens.spaceMd,
                        end = dimens.spaceMd
                    ),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
private fun ProductImageBoxPreview() {
    EasyMartTheme {
        ProductImageBox(
            imageUrl = null,
            contentDescription = stringResource(R.string.ui_text_077),
            modifier = Modifier.size(180.dp),
            showFallbackText = true
        )
    }
}
