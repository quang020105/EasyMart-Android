package com.example.easymart.presentation.ui.orderdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.order.OrderCancellationPolicy
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.common.components.ProductCard
import com.example.easymart.presentation.ui.common.components.RoundedActionButton
import com.example.easymart.presentation.ui.mock.mockOrders
import com.example.easymart.utils.toActionColor
import com.example.easymart.utils.toActionString
import com.example.easymart.utils.toDisplayString
import com.example.easymart.utils.toVNDString

@Composable
fun OrderPrimaryContent(
    modifier: Modifier = Modifier,
    order: Order,
    onRequestCancellation: () -> Unit = {},
) {
    val dimens = LocalAppDimens.current
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(dimens.radiusMedium),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.spaceXs)
    ) {
        Column(modifier = Modifier.padding(dimens.spaceMd)) {
            Text(
                text = "Thông tin giao hàng",
                style = MaterialTheme.typography.titleMedium
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = dimens.spaceSm),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )

            order.items.forEachIndexed { idx, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimens.spaceSm, vertical = dimens.spaceXs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProductCard(
                        modifier = Modifier
                            .size(dimens.cartImgSize)
                            .padding(end = dimens.spaceSm),
                        product = item.product,
                        onClick = {},
                        colorBackground = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.product.name,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "x${item.quantity}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    //giá tiền sản phẩm
                    Text(
                        text = item.totalPriceVnd.toVNDString(),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = dimens.spaceSm),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )

            //viết view cho tổng tiền và phương thức thanh toán ở đây

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimens.spaceSm, vertical = dimens.spaceXs),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(dimens.spaceXs),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Tổng cộng",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Phương thức thanh toán",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "Trạng thái thanh toán",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )

                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(dimens.spaceXs),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = order.totalAmount.toVNDString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = order.paymentMethod.toDisplayString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                    Text(
                        text = order.paymentStatus.toDisplayString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = dimens.spaceSm),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )

            //nút hành động
            val canCancelImmediately = OrderCancellationPolicy.canCustomerCancelImmediately(order)
            val actionText = if (OrderCancellationPolicy.canCustomerRequestCancellation(order)) {
                "Yêu cầu hủy đơn"
            } else {
                order.status.toActionString()
            }
            actionText?.let {
                RoundedActionButton(
                    text = it,
                    onClick = {
                        if (canCancelImmediately || OrderCancellationPolicy.canCustomerRequestCancellation(order)) {
                            onRequestCancellation()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dimens.spaceSm),
                    verticalPadding = dimens.spaceMd,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    backGroundColor = order.status.toActionColor()
                )
            }

        }
    }
}

@Preview
@Composable
fun OrderPrimaryContentPreview() {
    EasyMartTheme {
        OrderPrimaryContent(
            order = mockOrders[1]
        )
    }
}
