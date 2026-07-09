package com.example.easymart.presentation.ui.image_search.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.ImageSearch
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.colors.LocalAppColors
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun ImageSearchLoadingState(
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    StateSurface(modifier = modifier) {
        Surface(
            shape = RoundedCornerShape(dimens.radiusXl),
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            Box(
                modifier = Modifier
                    .padding(dimens.spaceMd)
                    .size(dimens.iconLarge),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(strokeWidth = dimens.dividerThickness)
                Icon(
                    imageVector = Icons.Rounded.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(dimens.iconMedium)
                )
            }
        }
        Text(
            text = "Đang tìm kiếm",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = LocalAppColors.current.textPrimary
        )
        Text(
            text = "AI đang phân tích ảnh và sắp xếp sản phẩm phù hợp",
            style = MaterialTheme.typography.bodyMedium,
            color = LocalAppColors.current.textSecondary,
            textAlign = TextAlign.Center
        )
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ImageSearchEmptyState(
    hasSelectedImage: Boolean,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    StateSurface(modifier = modifier) {
        Icon(
            imageVector = Icons.Rounded.ImageSearch,
            contentDescription = null,
            modifier = Modifier.size(dimens.iconLarge),
            tint = appColors.iconMuted
        )
        Text(
            text = if (hasSelectedImage) "Chưa có kết quả" else "Chưa có ảnh",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = appColors.textPrimary,
            textAlign = TextAlign.Center
        )
        Text(
            text = if (hasSelectedImage) "Nhấn Search để bắt đầu" else "Chọn ảnh từ Camera hoặc Gallery",
            style = MaterialTheme.typography.bodyMedium,
            color = appColors.textSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ImageSearchErrorState(
    message: String,
    retryEnabled: Boolean,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.radiusLarge),
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer
    ) {
        Row(
            modifier = Modifier.padding(dimens.spaceMd),
            horizontalArrangement = Arrangement.spacedBy(dimens.spaceMd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.WarningAmber,
                contentDescription = null,
                modifier = Modifier.size(dimens.iconMedium)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimens.spaceXs)
            ) {
                Text(
                    text = "Không thể tìm kiếm",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            OutlinedButton(
                onClick = onRetryClick,
                enabled = retryEnabled,
                shape = RoundedCornerShape(dimens.radiusMedium)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(dimens.iconSmall)
                )
                Text(
                    text = "Retry",
                    modifier = Modifier.padding(start = dimens.spaceXs)
                )
            }
        }
    }
}

@Composable
private fun StateSurface(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.radiusLarge),
        color = appColors.surfaceContainer,
        border = BorderStroke(dimens.dividerThickness, appColors.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(dimens.spaceXl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimens.spaceSm)
        ) {
            content()
        }
    }
}


@Preview(
    name = "Image Search Loading",
    showBackground = true
)
@Composable
private fun ImageSearchLoadingStatePreview() {
    EasyMartTheme {
        ImageSearchLoadingState(
            modifier = Modifier.padding(LocalAppDimens.current.spaceMd)
        )
    }
}

@Preview(
    name = "Image Search Empty",
    showBackground = true
)
@Composable
private fun ImageSearchEmptyStatePreview() {
    EasyMartTheme {
        ImageSearchEmptyState(
            hasSelectedImage = false,
            modifier = Modifier.padding(LocalAppDimens.current.spaceMd)
        )
    }
}

@Preview(
    name = "Image Search Error",
    showBackground = true
)
@Composable
private fun ImageSearchErrorStatePreview() {
    EasyMartTheme {
        ImageSearchErrorState(
            message = "Không thể kết nối đến máy chủ. Vui lòng thử lại.",
            retryEnabled = true,
            onRetryClick = {},
            modifier = Modifier.padding(LocalAppDimens.current.spaceMd)
        )
    }
}
