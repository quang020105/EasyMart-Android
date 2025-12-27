package com.example.easymart.presentation.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.R
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.utils.toVNDString

@Composable
fun ItemProductRecommendCard(
    product: Product,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    Card(
        modifier = modifier
            .width(dimens.recommendedWidth)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(dimens.radiusMedium)
            ).clickable(onClick = { onProductClick(product) }),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(dimens.radiusMedium),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProductCard(
                product = product,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.recommendedWidth),
                cornerRadius = 4.dp
            )
            Spacer(modifier = Modifier.height(dimens.spaceSm))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimens.spaceXs),
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(dimens.spaceXs))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.price.toVNDString(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = dimens.spaceSm)
                    )
                    Text(
                        text = "Đã bán ${product.soldQuantity}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                        modifier = Modifier.padding(bottom = dimens.spaceSm)
                    )
                }
            }

        }
    }
}

@Preview(showBackground = false)
@Composable
fun ItemProductRecommendCardPreview() {
    EasyMartTheme {
        ItemProductRecommendCard(
            product = Product(
                id = 1,
                name = "Giày sneaker",
                price = 9.99,
                imageRes = R.drawable.pic_shoe_1,
                description = "This is a sample product description.",
                imageUrl = "",
                soldQuantity = 15
            ),
            onProductClick = {}
        )
    }
}