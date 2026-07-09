package com.example.easymart.presentation.ui.image_search.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.font.FontWeight
import com.example.easymart.presentation.theme.colors.LocalAppColors
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun ImageSearchHeader(
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.radiusLarge),
        color = appColors.surfaceContainer,
        tonalElevation = dimens.cardElevation
    ) {
        Row(
            modifier = Modifier.padding(dimens.spaceLg),
            horizontalArrangement = Arrangement.spacedBy(dimens.spaceMd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(dimens.radiusMedium),
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
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
                verticalArrangement = Arrangement.spacedBy(dimens.spaceXs)
            ) {
                Text(
                    text = "Image Search",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = appColors.textPrimary
                )
                Text(
                    text = "Tìm sản phẩm bằng ảnh",
                    style = MaterialTheme.typography.bodyMedium,
                    color = appColors.textSecondary
                )
            }
        }
    }
}
