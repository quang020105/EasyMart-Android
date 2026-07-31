package com.example.easymart.presentation.ui.productdetail.components
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.colors.LocalAppColors
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
internal fun ProductDescriptionCard(
    description: String,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    SurfaceCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(dimens.spaceMd),
            verticalArrangement = Arrangement.spacedBy(dimens.spaceSm)
        ) {
            ProductDetailSectionHeader(
                title = stringResource(R.string.label_description),
                subtitle = stringResource(R.string.ui_text_318)
            )
            Text(
                text = description.ifBlank { stringResource(R.string.ui_text_319) },
                style = MaterialTheme.typography.bodyMedium,
                color = appColors.textSecondary
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductDescriptionCardPreview() {
    EasyMartTheme {
        ProductDescriptionCard(
            description = "Thiết kế hiện đại, chất liệu bền đẹp, phù hợp sử dụng hằng ngày."
        )
    }
}
