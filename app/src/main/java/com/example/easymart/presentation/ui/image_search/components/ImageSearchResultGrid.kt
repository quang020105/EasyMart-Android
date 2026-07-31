package com.example.easymart.presentation.ui.image_search.components
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.model.image_search.SimilarProduct
import com.example.easymart.presentation.theme.colors.LocalAppColors
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.common.components.ProductCard
import com.example.easymart.utils.toVNDString
import kotlin.math.roundToInt

@Composable
fun ImageSearchResultGrid(
    products: List<SimilarProduct>,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimens.spaceSm)
    ) {
        Text(
            text = stringResource(R.string.ui_text_286, products.size),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = appColors.textPrimary
        )

        products.chunked(2).forEach { rowProducts ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm)
            ) {
                rowProducts.forEach { item ->
                    ImageSearchProductCard(
                        item = item,
                        onProductClick = onProductClick,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowProducts.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun ImageSearchProductCard(
    item: SimilarProduct,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current
    val product = item.product

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.radiusLarge),
        colors = CardDefaults.cardColors(containerColor = appColors.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.cardElevation),
        border = BorderStroke(dimens.dividerThickness, appColors.outlineVariant),
        onClick = { onProductClick(product) }
    ) {
        Column(
            modifier = Modifier.padding(dimens.spaceSm),
            verticalArrangement = Arrangement.spacedBy(dimens.spaceSm)
        ) {
            ProductCard(
                product = product,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.recommendedHeight),
                cornerRadius = dimens.radiusMedium,
                colorBackground = appColors.surfaceContainerHigh,
                onClick = onProductClick
            )

            Text(
                text = product.name,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = appColors.textPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = product.priceVnd.toVNDString(),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimens.spaceXs),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = appColors.warning,
                    modifier = Modifier.padding(end = dimens.spaceXs)
                )
                Text(
                    text = if (product.rating.rate > 0) product.rating.rate.toString() else stringResource(R.string.ui_text_242),
                    style = MaterialTheme.typography.labelMedium,
                    color = appColors.textSecondary,
                    modifier = Modifier.weight(1f)
                )
                MatchScoreChip(score = item.score)
            }
        }
    }
}

@Composable
private fun MatchScoreChip(score: Double) {
    val dimens = LocalAppDimens.current
    val displayScore = if (score <= 1.0) {
        (score * 100).roundToInt()
    } else {
        score.roundToInt()
    }.coerceIn(0, 100)

    Surface(
        shape = RoundedCornerShape(dimens.radiusXl),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Text(
            text = stringResource(R.string.ui_text_287, displayScore),
            modifier = Modifier.padding(
                horizontal = dimens.spaceSm,
                vertical = dimens.spaceXs
            ),
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}
