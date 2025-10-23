package com.example.easymart.presentation.ui.checkout.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.easymart.R
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.common.components.ProductCard

@Composable
fun PaymentProduct(
    modifier: Modifier = Modifier,
    product: Product,
    quantity: Int = 1
) {
    val dimens = LocalAppDimens.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = dimens.spaceXs)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                shape = RoundedCornerShape(dimens.radiusMedium)
            ),
        shape = RoundedCornerShape(dimens.radiusMedium),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimens.spaceMd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductCard(
                modifier = Modifier
                    .padding(all = dimens.spaceXs)
                    .size(dimens.cartImgSize),
                product = product,
                onClick = {},
                colorBackground = MaterialTheme.colorScheme.background
            )
            Column(
                modifier = Modifier
                    .padding(all = dimens.spaceSm).weight(1f)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2
                )
                Text(
                    text = "$${product.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = dimens.spaceSm)
                )
            }
            Text(
                text = "x$quantity",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(end = dimens.spaceMd, bottom = dimens.spaceMd).align(Alignment.Bottom)
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun PaymentProductPreview() {
    val dimens = LocalAppDimens.current
    EasyMartTheme {
        PaymentProduct(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimens.spaceMd),
            product = Product(
                id = 1,
                name = "Sample Product",
                price = 29.99,
                imageUrl = "",
                description = "This is a sample product description.",
                imageRes = R.drawable.pic_shoe_1
            ),
            quantity = 2
        )
    }
}