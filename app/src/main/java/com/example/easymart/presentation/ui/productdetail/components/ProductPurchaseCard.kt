package com.example.easymart.presentation.ui.productdetail.components
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.colors.LocalAppColors
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.mock.mockProducts
import com.example.easymart.utils.toVNDString

@Composable
internal fun ProductPurchaseCard(
    product: Product,
    quantity: Int,
    inStock: Boolean,
    onMinusClick: () -> Unit,
    onPlusClick: () -> Unit,
    onAddToCartClick: () -> Unit,
    onBuyNowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current
    val canIncrease = inStock && quantity < product.stockQuantity
    val totalPriceVnd = product.priceVnd * quantity

    SurfaceCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(dimens.spaceMd),
            verticalArrangement = Arrangement.spacedBy(dimens.spaceMd)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(dimens.spaceXs)) {
                    Text(
                        text = stringResource(R.string.label_quantity),
                        style = MaterialTheme.typography.labelLarge,
                        color = appColors.textSecondary
                    )
                    Text(
                        text = stringResource(R.string.ui_text_325, totalPriceVnd.toVNDString()),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = appColors.textPrimary
                    )
                }

                QuantitySelector(
                    quantity = quantity,
                    canDecrease = quantity > 1,
                    canIncrease = canIncrease,
                    onMinusClick = onMinusClick,
                    onPlusClick = onPlusClick
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalButton(
                    onClick = onAddToCartClick,
                    enabled = inStock,
                    modifier = Modifier
                        .weight(1f)
                        .height(dimens.buttonHeight),
                    shape = RoundedCornerShape(dimens.radiusMedium),
                    contentPadding = PaddingValues(horizontal = dimens.spaceSm)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(dimens.iconSmall)
                    )
                    Spacer(modifier = Modifier.width(dimens.spaceXs))
                    Text(
                        text = stringResource(R.string.ui_text_326),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Button(
                    onClick = onBuyNowClick,
                    enabled = inStock,
                    modifier = Modifier
                        .weight(1f)
                        .height(dimens.buttonHeight),
                    shape = RoundedCornerShape(dimens.radiusMedium),
                    contentPadding = PaddingValues(horizontal = dimens.spaceSm)
                ) {
                    Text(
                        text = if (inStock) stringResource(R.string.action_buy_now) else stringResource(R.string.ui_text_275),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun QuantitySelector(
    quantity: Int,
    canDecrease: Boolean,
    canIncrease: Boolean,
    onMinusClick: () -> Unit,
    onPlusClick: () -> Unit
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    Surface(
        shape = RoundedCornerShape(dimens.radiusXl),
        color = appColors.surfaceContainerHigh,
        border = BorderStroke(
            width = dimens.dividerThickness,
            color = appColors.outlineVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(dimens.spaceXs),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.spaceXs)
        ) {
            QuantityButton(
                text = "-",
                enabled = canDecrease,
                onClick = onMinusClick
            )
            Text(
                text = quantity.toString(),
                modifier = Modifier.width(dimens.buttonHeight),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = appColors.textPrimary,
                textAlign = TextAlign.Center
            )
            QuantityButton(
                text = "+",
                enabled = canIncrease,
                onClick = onPlusClick
            )
        }
    }
}

@Composable
private fun QuantityButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.size(dimens.buttonHeight),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (enabled) MaterialTheme.colorScheme.surface else appColors.disabledContainer,
            contentColor = if (enabled) appColors.textPrimary else appColors.disabledContent
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductPurchaseCardPreview() {
    val product = mockProducts.first()
    EasyMartTheme {
        ProductPurchaseCard(
            product = product,
            quantity = 2,
            inStock = product.stockQuantity > 0,
            onMinusClick = {},
            onPlusClick = {},
            onAddToCartClick = {},
            onBuyNowClick = {}
        )
    }
}
