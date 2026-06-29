package com.example.easymart.presentation.ui.productdetail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.colors.LocalAppColors
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
internal fun DetailInfoBanner(
    message: String,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.radiusMedium),
        color = appColors.infoContainer
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(dimens.spaceMd),
            style = MaterialTheme.typography.bodyMedium,
            color = appColors.info
        )
    }
}

@Composable
internal fun SimilarLoadingCard(modifier: Modifier = Modifier) {
    val dimens = LocalAppDimens.current

    SurfaceCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(dimens.spaceMd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm)
        ) {
            CircularProgressIndicator(modifier = Modifier.size(dimens.iconMedium))
            Text(
                text = "Đang tìm sản phẩm tương tự...",
                style = MaterialTheme.typography.bodyMedium,
                color = LocalAppColors.current.textSecondary
            )
        }
    }
}

@Composable
internal fun EmptyDetailState(
    message: String,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.screenPadding),
            shape = RoundedCornerShape(dimens.radiusLarge),
            color = appColors.surfaceContainer,
            border = BorderStroke(
                width = dimens.dividerThickness,
                color = appColors.outlineVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(dimens.spaceXl),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimens.spaceSm)
            ) {
                Text(
                    text = "Không tìm thấy sản phẩm",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = appColors.textPrimary
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = appColors.textSecondary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailInfoBannerPreview() {
    EasyMartTheme {
        DetailInfoBanner(message = "Không thể làm mới dữ liệu sản phẩm.")
    }
}

@Preview(showBackground = true)
@Composable
private fun SimilarLoadingCardPreview() {
    EasyMartTheme {
        SimilarLoadingCard()
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyDetailStatePreview() {
    EasyMartTheme {
        EmptyDetailState(message = "Không có dữ liệu sản phẩm")
    }
}
