package com.example.easymart.presentation.ui.image_search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.colors.LocalAppColors
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun ImageSearchHeader(
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current
    val headerShape = RoundedCornerShape(dimens.radiusLarge)
    val headerBrush = Brush.linearGradient(
        colors = listOf(
            appColors.aiAccent,
            appColors.primaryGradientStart,
            MaterialTheme.colorScheme.primary
        )
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = headerShape,
        color = Color.Transparent,
        shadowElevation = dimens.cardElevation
    ) {
        Row(
            modifier = Modifier
                .background(headerBrush)
                .padding(dimens.spaceLg),
            horizontalArrangement = Arrangement.spacedBy(dimens.spaceMd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(dimens.radiusMedium),
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.16f),
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Rounded.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(dimens.spaceSm)
                        .size(dimens.iconMedium)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimens.spaceXs)
            ) {
                Text(
                    text = "Image Search",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = "Tìm sản phẩm bằng ảnh",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.84f)
                )
            }

            Box(
                modifier = Modifier
                    .size(dimens.iconSmall)
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.22f),
                        shape = RoundedCornerShape(dimens.radiusSmall)
                    )
            )
        }
    }
}

@Preview
@Composable
fun ImageSearchHeaderPreview() {
    EasyMartTheme {
        ImageSearchHeader()
    }
}
