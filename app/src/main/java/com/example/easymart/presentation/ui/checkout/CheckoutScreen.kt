package com.example.easymart.presentation.ui.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.R
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.checkout.components.PaymentProduct
import com.example.easymart.presentation.ui.common.components.RoundedActionButton

@Composable
fun CheckoutScreen(
    modifier: Modifier = Modifier,
    products: List<Product> = emptyList(),
    onAddressClick: () -> Unit = {},
    onPaymentClick: () -> Unit = {},
    subTotal: Double = 0.0,
    shipping: Double = 0.0,
    total: Double = 0.0,
    onConfirmClick: () -> Unit = {}
) {
    val dimens = LocalAppDimens.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
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
                    ),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(dimens.radiusLarge)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = dimens.spaceMd)
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
                        Text(
                            text = "123 Đường ABC, Phường XYZ, Quận 1, TP.HCM",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = dimens.spaceMd)
                        )
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
                    ),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(dimens.radiusLarge)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = dimens.spaceMd)
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
                        Icon(
                            painter = painterResource(id = R.drawable.ic_wallet),
                            contentDescription = "Biểu tượng ví",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = "Ví Momo",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = dimens.spaceMd)
                        )
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
        items(items = products, key = { it.id }) { product ->
            PaymentProduct(product = product, quantity = 1)
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
                        text = "$subTotal Đ",
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
                        text = "$total Đ",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }


        // Footer: Confirm & Pay button as item to appear after products
//        item {
//            Spacer(modifier = Modifier.height(dimens.spaceLarge))
//            Button(
//                onClick = onConfirmClick,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = dimens.spaceMd)
//                    .padding(bottom = dimens.spaceLarge),
//                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
//                shape = RoundedCornerShape(dimens.radiusMedium),
//                contentPadding = PaddingValues(vertical = dimens.buttonPaddingV)
//            ) {
//                Text(text = "Xác nhận & Thanh toán", color = MaterialTheme.colorScheme.onPrimary)
//            }
//        }
    }
}


@Preview
@Composable
fun CheckoutScreenPreview() {
    EasyMartTheme {
        CheckoutScreen(
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
            subTotal = 290.0,
            shipping = 10.0,
            total = 300.0
        )
    }
}