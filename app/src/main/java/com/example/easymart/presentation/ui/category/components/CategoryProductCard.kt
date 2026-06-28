package com.example.easymart.presentation.ui.category.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.R
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.common.components.ProductCard
import com.example.easymart.presentation.ui.mock.mockSimpleProduct
import com.example.easymart.utils.toVNDString

@Composable
fun CategoryProductCard(
    product: Product,
    onProductClick: (Product) -> Unit,
    onAddToCartClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.radiusMedium),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = { onProductClick(product) }
    ) {
        Column(
            modifier = Modifier.padding(dimens.spaceSm)
        ) {
            ProductCard(
                product = product,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.recommendedWidth),
                cornerRadius = dimens.radiusSmall,
                colorBackground = MaterialTheme.colorScheme.surfaceVariant,
                onClick = onProductClick
            )
            Spacer(modifier = Modifier.height(dimens.spaceSm))
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(dimens.spaceXs))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.price.toVNDString(),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(dimens.spaceXs))
                IconButton(
                    onClick = { onAddToCartClick(product) },
                    modifier = Modifier.size(dimens.iconLarge)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add_to_cart),
                        contentDescription = "Thêm vào giỏ",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CategoryProductCardPreview() {
    CategoryProductCard(
        product = mockSimpleProduct,
        onProductClick = {},
        onAddToCartClick = {}
    )
}
