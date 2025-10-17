package com.example.easymart.presentation.ui.order

import android.widget.Space
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.R
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.common.components.ProductCard
import com.example.easymart.presentation.ui.common.components.RoundedActionButton

@Composable
fun OrderCard(
    modifier: Modifier = Modifier,
    product: Product,
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
                .padding(top = dimens.spaceMd),
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
                    .padding(all = dimens.spaceSm)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2
                )
                Text(
                    text = "Tổng số tiền (x) sản phẩm: ${product.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = dimens.spaceSm)
                )
                Text(
                    text = "Thời gian mua",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.padding(vertical = dimens.spaceSm)
                )
            }

            Surface(
                modifier = Modifier
                    .padding(end = dimens.spaceMd, bottom = dimens.spaceMd)
                    .wrapContentWidth()
                    .align(Alignment.Bottom),
                shape = RoundedCornerShape(dimens.radiusSmall),
                shadowElevation = dimens.cardElevation,
                color = MaterialTheme.colorScheme.background
            ) {

            }
        }
        Row (
            modifier = Modifier.fillMaxWidth().padding(bottom = dimens.spaceSm),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ){
            RoundedActionButton(
                text = "Mua lại",
                onClick = {},
                horizontalPadding = dimens.spaceLg,
                alpha = 0.9f
            )
            Spacer(modifier = Modifier.size(dimens.spaceSm))
            RoundedActionButton(
                text = "Chi tiết",
                onClick = {},
                horizontalPadding = dimens.spaceLg,
                alpha = 0.9f
            )
            Spacer(modifier = Modifier.size(dimens.spaceMd))
        }
    }
}

@Preview(showBackground = false)
@Composable
fun OrderCardPreview() {
    EasyMartTheme {
        OrderCard(
            product = Product(
                id = 1,
                name = "Sample Product",
                price = 9.99,
                imageRes = R.drawable.pic_shoe_1,
                description = "This is a sample product description.",
                imageUrl = ""
            )
        )
    }
}