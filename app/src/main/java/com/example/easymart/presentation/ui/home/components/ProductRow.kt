package com.example.easymart.presentation.ui.home.components

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.R
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.common.components.RoundedActionButton
import com.example.easymart.utils.toVNDString

@Composable
fun ProductRow(
    product: Product,
    onAddToCart: (Product) -> Unit
) {
    val dimens = LocalAppDimens.current
    Row (
        modifier = Modifier.fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(vertical = dimens.spaceLg),
        verticalAlignment = Alignment.CenterVertically
    ){
        Column (
            modifier = Modifier
                .weight(1f)
        ){
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.padding(top = dimens.spaceSm))
            Text(
                text = product.price.toVNDString(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        RoundedActionButton(
            text = "Thêm vào giỏ hàng",
            onClick = { onAddToCart(product) },
            minHeight = dimens.buttonHeight,
            horizontalPadding = dimens.spaceLg,
            cornerRadius = dimens.radiusMedium
        )
    }
}

@Preview
@Composable
fun ProductRowPreview() {
    EasyMartTheme {
        ProductRow(
            product = Product(
                id = 1,
                name = "Sample Product",
                description = "This is a sample product description.",
                price = 304.5,
                imageRes = R.drawable.pic_shoe_1,
                imageUrl = ""
            ),
            onAddToCart = {}
        )
    }
}