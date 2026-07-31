package com.example.easymart.presentation.ui.productdetail.components
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.colors.LocalAppColors
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
internal fun SurfaceCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(dimens.radiusLarge),
        color = appColors.surfaceContainer,
        tonalElevation = dimens.dividerThickness,
        shadowElevation = dimens.cardElevation,
        border = BorderStroke(
            width = dimens.dividerThickness,
            color = appColors.outlineVariant
        ),
        content = content
    )
}

@Composable
internal fun ProductDetailSectionHeader(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimens.spaceXs)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = appColors.textPrimary
        )
        if(subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = appColors.textSecondary
            )
        }
    }
}

@Composable
internal fun InfoChip(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(dimens.radiusLarge),
        color = appColors.selected,
        contentColor = appColors.onSelected
    ) {
        Row(
            modifier = Modifier.padding(dimens.spaceSm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.spaceXs)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(dimens.iconSmall)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
internal fun IconBadge(icon: ImageVector) {
    val dimens = LocalAppDimens.current

    Box(
        modifier = Modifier
            .size(dimens.iconLarge)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(dimens.iconSmall),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductDetailSharedComponentsPreview() {
    EasyMartTheme {
        SurfaceCard {
            Column(
                modifier = Modifier.padding(LocalAppDimens.current.spaceMd),
                verticalArrangement = Arrangement.spacedBy(LocalAppDimens.current.spaceSm)
            ) {
                ProductDetailSectionHeader(
                    title = stringResource(R.string.label_description),
                    subtitle = stringResource(R.string.ui_text_318)
                )
                InfoChip(
                    text = stringResource(R.string.ui_text_320),
                    icon = Icons.Filled.Verified
                )
            }
        }
    }
}
