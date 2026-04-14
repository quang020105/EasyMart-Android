package com.example.easymart.presentation.ui.checkout

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.R
import com.example.easymart.domain.model.Address
import com.example.easymart.domain.model.CartItem
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.checkout.components.PaymentProduct
import com.example.easymart.presentation.ui.common.components.RoundedActionButton
import com.example.easymart.presentation.ui.mock.mockCartItems
import com.example.easymart.presentation.ui.payment.mapper.PaymentMethodUiMapper
import com.example.easymart.utils.toVNDString

@Composable
fun CheckoutScreen(
    modifier: Modifier = Modifier,
    address: Address? = null,
    paymentMethod: PaymentMethod? = null,
    cartItems: List<CartItem> = emptyList(),
    subTotal: Double = 0.0,
    shipping: Double = 0.0,
    total: Double = 0.0,
    isLoading: Boolean = false,
    isAddressLoading: Boolean = false,
    onAddressClick: () -> Unit = {},
    onPaymentClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {}
) {
    val dimens = LocalAppDimens.current


    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(color = MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(all = dimens.spaceMd)
        ) {
            // Address header (item)
            item {
                Spacer(modifier = Modifier.height(dimens.spaceMd))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(dimens.radiusLarge)
                        )
                        .clickable(onClick = onAddressClick),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(dimens.radiusLarge)
                ) {
                    when {
                        isAddressLoading -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 120.dp)
                                    .padding(all = dimens.spaceLg),
                                verticalArrangement = Arrangement.spacedBy(dimens.spaceSm)
                            ) {
                                ShimmerPlaceholder(
                                    modifier = Modifier
                                        .fillMaxWidth(0.6f)
                                        .height(18.dp)
                                )
                                ShimmerPlaceholder(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(14.dp)
                                )
                                ShimmerPlaceholder(
                                    modifier = Modifier
                                        .fillMaxWidth(0.75f)
                                        .height(14.dp)
                                )
                            }
                        }
                        address == null -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 120.dp)
                                    .padding(all = dimens.spaceLg),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_address),
                                    contentDescription = "Địa chỉ giao hàng",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Bạn chưa có địa chỉ giao hàng",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(top = dimens.spaceSm)
                                )
                                Text(
                                    text = "Thêm địa chỉ để tiếp tục thanh toán",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = dimens.spaceXs)
                                )
                                FilledTonalButton(
                                    onClick = onAddressClick,
                                    modifier = Modifier.padding(top = dimens.spaceSm),
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                        contentColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_add),
                                        contentDescription = "Thêm địa chỉ"
                                    )
                                    Spacer(modifier = Modifier.width(dimens.spaceSm))
                                    Text(text = "Thêm địa chỉ")
                                }
                            }
                        }
                        else -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = dimens.spaceLg)
                            ) {
                                Text(
                                    text = "Địa chỉ giao hàng",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(bottom = dimens.spaceSm)
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = dimens.spaceMd),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_address),
                                        contentDescription = "Biểu tượng địa chỉ",
                                        tint = MaterialTheme.colorScheme.primary,
                                    )

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = dimens.spaceLg)
                                    ) {

                                        // -------- NAME + PHONE --------
                                        Row(
                                            modifier = Modifier
                                                .padding(vertical = dimens.spaceXs)
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = address.name,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                fontWeight = FontWeight.Medium
                                            )
                                            VerticalDivider(
                                                modifier = Modifier
                                                    .padding(horizontal = dimens.spaceSm)
                                                    .height(16.dp),
                                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                                    alpha = 0.5f
                                                ),
                                                thickness = 1.dp
                                            )
                                            Text(
                                                text = address.phone,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                                    alpha = 0.7f
                                                )
                                            )
                                        }


                                        Text(
                                            text = address.addressString,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(vertical = dimens.spaceXs)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(dimens.spaceXl))
            }

            // Payment header
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(dimens.radiusLarge)
                        )
                        .clickable(onClick = onPaymentClick),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(dimens.radiusLarge)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = dimens.spaceLg)
                    ) {
                        Text(
                            text = "Phương thức thanh toán",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = dimens.spaceSm)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = dimens.spaceMd),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            //lấy dữ liệu từ paymentMethod để hiển thị
                            val paymentUi = if (paymentMethod != null) {
                                PaymentMethodUiMapper.map(paymentMethod)
                            } else {
                                PaymentMethodUiMapper.map(PaymentMethod.COD)
                            }

                            Icon(
                                painter = painterResource(id = paymentUi.iconRes),
                                contentDescription = "Biểu tượng ví",
                                tint = MaterialTheme.colorScheme.primary,
                            )
                            Column(
                                modifier = Modifier.padding(start = dimens.spaceMd)
                            ) {
                                Text(
                                    text = stringResource(id = paymentUi.titleRes),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(dimens.spaceSm))
                                Text(
                                    text = stringResource(id = paymentUi.descriptionRes),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(dimens.spaceXl))
            }

            // sản phẩm
            item {
                Text(
                    text = "Sản phẩm chọn mua",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = dimens.spaceMd)
                )
            }
            items(items = cartItems, key = { it.id }) { cartItem ->
                PaymentProduct(cartItem = cartItem)
            }

            // Order summary header
            item {
                Text(
                    text = "Tóm tắt đơn hàng",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimens.spaceMd,
                            end = dimens.spaceMd,
                            top = dimens.spaceXl,
                        )
                )

                Spacer(modifier = Modifier.height(dimens.spaceSm))

                Column(
                    modifier = modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(horizontal = dimens.spaceMd)
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
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = subTotal.toVNDString(),
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
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = shipping.toVNDString(),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(dimens.spaceSm)
                    )
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
                            text = "Tổng tiền: ",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Text(
                            text = total.toVNDString(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(bottom = dimens.spaceXs),
            horizontalAlignment = Alignment.End
        ) {
            if (address == null && !isAddressLoading) {
                Text(
                    text = "Vui lòng thêm địa chỉ giao hàng trước khi đặt hàng",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = dimens.spaceMd, vertical = dimens.spaceXs)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(text = "Tổng cộng: ", style = MaterialTheme.typography.titleSmall)
                Text(
                    text = total.toVNDString(), color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge
                )
                RoundedActionButton(
                    text = if (isLoading) "Đang xử lý" else "Thanh toán",
                    enabled = !isLoading && address != null,
                    onClick = onConfirmClick,
                    modifier = Modifier.padding(horizontal = dimens.spaceMd, vertical = dimens.spaceXs)
                )
            }
        }
    }

}


@Preview
@Composable
fun CheckoutScreenPreview() {
    EasyMartTheme {
        CheckoutScreen(
            address = Address(
                name = "Nguyễn Văn A",
                phone = "0367558301",
                addressString = "123 Đường ABC, Phường XYZ, Quận 1, TP. Hồ Chí Minh",
            ),
            paymentMethod = PaymentMethod.COD,
            cartItems = mockCartItems,
            subTotal = 290.0,
            shipping = 10.0,
            total = 300.0,
            isAddressLoading = false
        )
    }
}

@Composable
private fun ShimmerPlaceholder(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(6.dp)
) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translate = transition.animateFloat(
        initialValue = 0f,
        targetValue = 600f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )
    val shimmerColors = listOf(
        Color(0xFFEEEEEE),
        Color(0xFFF6F6F6),
        Color(0xFFEEEEEE)
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translate.value - 200f, 0f),
        end = Offset(translate.value, 0f)
    )

    Box(
        modifier = modifier.background(brush, shape)
    )
}
