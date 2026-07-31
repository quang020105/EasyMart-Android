package com.example.easymart.presentation.ui.admin.orders.detail.components
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.presentation.theme.EasyMartTheme

@Immutable
data class OrderDetailItemUiModel(
    val productName: String,
    val imageUrl: String?,
    val priceAtPurchaseText: String,
    val quantityText: String,
    val lineTotalText: String
)

@Composable
fun OrderItemsSection(
    items: List<OrderDetailItemUiModel>,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        SectionTitle(
            icon = Icons.Rounded.ShoppingBag,
            title = stringResource(R.string.ui_text_047)
        )

        Column {
            items.forEachIndexed { index, item ->
                OrderItemRow(
                    productName = item.productName,
                    imageUrl = item.imageUrl,
                    priceAtPurchaseText = item.priceAtPurchaseText,
                    quantityText = item.quantityText,
                    lineTotalText = item.lineTotalText,
                    modifier = Modifier.padding(vertical = 10.dp)
                )

                if (index != items.lastIndex) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OrderItemsSectionPreview() {
    EasyMartTheme {
        OrderItemsSection(
            items = listOf(
                OrderDetailItemUiModel(
                    productName = "Tai nghe Bluetooth Pro X1",
                    imageUrl = null,
        priceAtPurchaseText = stringResource(R.string.ui_text_349),
                    quantityText = "1",
        lineTotalText = stringResource(R.string.ui_text_349)
                ),
                OrderDetailItemUiModel(
                    productName = "Bàn phím cơ Mini K68",
                    imageUrl = null,
        priceAtPurchaseText = stringResource(R.string.ui_text_350),
                    quantityText = "1",
        lineTotalText = stringResource(R.string.ui_text_350)
                )
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
