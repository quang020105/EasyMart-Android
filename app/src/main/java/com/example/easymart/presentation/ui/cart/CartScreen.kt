package com.example.easymart.presentation.ui.cart

import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.R
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.cart.components.ProductCart
import com.example.easymart.presentation.ui.common.components.RoundedActionButton

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    onCheckOutCLick: () -> Unit,
    subtotal: Double = 0.0,
    shipping: Double = 0.0,
    total: Double = 0.0,
    allChecked: Boolean = false,
    products: List<Product> = emptyList(),
    onChangeCheckedAll: (Boolean) -> Unit = {},
    onProductClick: (Product) -> Unit = {},
    onPlusClick: (productId: String) -> Unit = {},
    onMinusClick: (productId: String) -> Unit = {},
    onCheckedChange: (productId: String, checked: Boolean) -> Unit = {_,_ -> },
) {
    val dimens = LocalAppDimens.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        LazyColumn(
            modifier = modifier.padding(horizontal = dimens.spaceMd),

            ) {
            items(products, key = { it.id }) { product ->
                ProductCart(
                    product = product,
                    onMinusClick = { onMinusClick(product.id.toString()) },
                    onPlusClick = { onPlusClick(product.id.toString()) },
                    onCheckedChange = { checked -> onCheckedChange(product.id.toString(), checked) }
                )
            }
        }
        Column(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(horizontal = dimens.spaceLg)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = dimens.spaceSm),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Tổng tiền sản phẩm: ",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = "$subtotal Đ",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = dimens.spaceSm),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Phí vận chuyển : ",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = "$shipping Đ",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            Spacer(
                modifier = Modifier
                    .height(dimens.spaceSm)
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth().padding(vertical = dimens.spaceSm),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = dimens.spaceSm),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Tổng tiền: ",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = "$total Đ",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = dimens.spaceSm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = allChecked,
                    onCheckedChange = onChangeCheckedAll,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.onBackground
                    )
                )
                Text(
                    text = "Chọn tất cả",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                RoundedActionButton(
                    text = "Mua hàng",
                    onClick = onCheckOutCLick,
                    modifier = Modifier.fillMaxWidth(0.4f),
                )
            }
        }
    }

}

@Preview
@Composable
fun CartScreenPreview() {
    EasyMartTheme {
        CartScreen(
            onCheckOutCLick = {},
            products = listOf(
                Product(
                    1,
                    "Product 1",
                    "Description 1",
                    10.0,
                    "https://cdn.dummyjson.com/product-images/beauty/essence-mascara-lash-princess/1.webp",
                    R.drawable.pic_shoe_1
                ),
                Product(
                    2,
                    "Product 2",
                    "Description 2",
                    20.0,
                    "https://cdn.dummyjson.com/product-images/beauty/essence-mascara-lash-princess/1.webp",
                    R.drawable.pic_shoe_1
                ),
                Product(
                    3,
                    "Product 3",
                    "Description 3",
                    30.0,
                    "https://cdn.dummyjson.com/product-images/beauty/essence-mascara-lash-princess/1.webp",
                    R.drawable.pic_shoe_1
                )
            ),
            onChangeCheckedAll = {},
            onPlusClick = {},
            onMinusClick = {},
            onCheckedChange = { _, _ -> },
            subtotal = 60.0,
            shipping = 5.0,
            total = 65.0
        )
    }
}

