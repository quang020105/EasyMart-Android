package com.example.easymart.presentation.ui.image_search.components
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

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
            color = LocalAppColors.current.aiAccentSoft,
            contentColor = LocalAppColors.current.aiAccent,
            border = BorderStroke(
                dimens.dividerThickness,
                LocalAppColors.current.aiAccent.copy(alpha = 0.32f)
            )
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
            text = stringResource(R.string.ui_text_288),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = LocalAppColors.current.textPrimary
        )
        Text(
            text = stringResource(R.string.ui_text_289),
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
            text = if (hasSelectedImage) stringResource(R.string.ui_text_290) else stringResource(R.string.ui_text_291),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = appColors.textPrimary,
            textAlign = TextAlign.Center
        )
        Text(
            text = if (hasSelectedImage) stringResource(R.string.ui_text_292) else stringResource(R.string.ui_text_293),
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
            Surface(
                shape = RoundedCornerShape(dimens.radiusMedium),
                color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.12f),
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                border = BorderStroke(
                    dimens.dividerThickness,
                    MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.24f)
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.WarningAmber,
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
                    text = stringResource(R.string.ui_text_294),
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
                    text = stringResource(R.string.ui_text_295),
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
        border = BorderStroke(dimens.dividerThickness, appColors.aiAccent.copy(alpha = 0.18f))
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
            message = stringResource(R.string.ui_text_296),
            retryEnabled = true,
            onRetryClick = {},
            modifier = Modifier.padding(LocalAppDimens.current.spaceMd)
        )
    }
}
