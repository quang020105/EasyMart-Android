package com.example.easymart.presentation.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.R
import com.example.easymart.domain.model.CartItem
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.cart.components.ProductCart
import com.example.easymart.presentation.ui.common.components.RoundedActionButton
import com.example.easymart.presentation.ui.mock.mockCartItems
import com.example.easymart.utils.toVNDString

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    uiState: CartUiState,
    onCheckOutClick: () -> Unit,
    onConfirmRemove: () -> Unit = {},
    onCancelRemove: () -> Unit = {},
    onConfirmRemoveSelected: () -> Unit = {},
    onCartItemClick: (product: Product) -> Unit = {},
    onCancelRemoveSelected: () -> Unit = {},
    onChangeCheckedAll: (Boolean) -> Unit = {},
    onPlusClick: (cartItem: CartItem) -> Unit = {},
    onMinusClick: (cartItem: CartItem) -> Unit = {},
    onCheckedChange: (cartItem: CartItem, checked: Boolean) -> Unit = { _, _ -> },
) {
    val dimens = LocalAppDimens.current
    val selectedCount = uiState.selectedItems.size
    if (uiState.pendingRemoveItem != null) {
        AlertDialog(
            onDismissRequest = onCancelRemove,
            title = { Text(text = "Xóa sản phẩm?") },
            text = { Text(text = "Bạn có muốn xóa sản phẩm này khỏi giỏ hàng không?") },
            confirmButton = {
                TextButton(onClick = onConfirmRemove) {
                    Text(text = "Xóa")
                }
            },
            dismissButton = {
                TextButton(onClick = onCancelRemove) {
                    Text(text = "Hủy")
                }
            }
        )
    }

    if (uiState.pendingRemoveSelected) {
        AlertDialog(
            onDismissRequest = onCancelRemoveSelected,
            title = { Text(text = "Xóa $selectedCount sản phẩm?") },
            text = { Text(text = "Bạn có chắc chắn muốn xóa các sản phẩm đã chọn khỏi giỏ hàng không?") },
            confirmButton = {
                TextButton(onClick = onConfirmRemoveSelected) {
                    Text(text = "Xóa")
                }
            },
            dismissButton = {
                TextButton(onClick = onCancelRemoveSelected) {
                    Text(text = "Hủy")
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        when {
            uiState.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.isEmpty -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.pic_empty_cart),
                        contentDescription = "Giỏ hàng trống"
                    )
                    Text(
                        text = "Giỏ hàng trống",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = dimens.spaceSm)
                    )
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = dimens.spaceMd)
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(bottom = dimens.spaceXl)
                ) {
                    items(uiState.items, key = { it.id }) { cartItem ->
                        ProductCart(
                            cartItem = cartItem,
                            checked = cartItem.isChecked,
                            onProductClick = { onCartItemClick(cartItem.product) },
                            onMinusClick = { onMinusClick(cartItem) },
                            onPlusClick = { onPlusClick(cartItem) },
                            onCheckedChange = { checked ->
                                onCheckedChange(
                                    cartItem,
                                    checked
                                )
                            }
                        )
                    }
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimens.spaceMd, vertical = dimens.spaceSm),
            shape = RoundedCornerShape(dimens.radiusLarge),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = dimens.cardElevation)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = dimens.spaceLg, vertical = dimens.spaceMd)
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = dimens.spaceXs),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Tạm tính",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                        text = uiState.subtotal.toVNDString(),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = dimens.spaceXs),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Phí vận chuyển",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                        text = uiState.shipping.toVNDString(),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = dimens.spaceXs),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Giảm giá",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                        text = 0L.toVNDString(),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimens.spaceSm),
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
                        text = "Tổng thanh toán",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = uiState.total.toVNDString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = dimens.spaceSm),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = uiState.checkedAll,
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
                        onClick = onCheckOutClick,
                        modifier = Modifier.fillMaxWidth(0.45f),
                        enabled = uiState.selectedItems.isNotEmpty(),
                        alpha = if (uiState.selectedItems.isNotEmpty()) 1f else 0.65f
                    )
                }
                if (uiState.selectedItems.isEmpty()) {
                    Text(
                        text = "Chọn ít nhất 1 sản phẩm để thanh toán.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = dimens.spaceXs)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CartScreenPreview() {
    EasyMartTheme {
        CartScreen(
            onCheckOutClick = {},
            uiState = CartUiState(items = mockCartItems)
        )
    }
}


//tôi muốn trong thanh topBar của màn hình cart có nút xóa các item được chọn
