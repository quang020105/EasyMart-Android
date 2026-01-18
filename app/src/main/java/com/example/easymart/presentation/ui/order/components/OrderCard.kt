package com.example.easymart.presentation.ui.order.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.common.components.ProductCard
import com.example.easymart.presentation.ui.common.components.RoundedActionButton
import com.example.easymart.presentation.ui.mock.mockOrders
import com.example.easymart.presentation.ui.order.extension.toColor
import com.example.easymart.presentation.ui.order.extension.toPrimaryActionText
import com.example.easymart.utils.toVNDString

@Composable
fun OrderCard(
    modifier: Modifier = Modifier,
    order: Order,
    onPrimaryAction: () -> Unit = {},
    onSecondaryAction: () -> Unit = {},
    onOpenDetail: () -> Unit = {}
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
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.spaceXs)
    ) {

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = dimens.spaceMd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //mã đơn hàng và thời gian đặt hàng
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Đơn #" + order.id.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(dimens.spaceXs))
                    Text(
                        text = order.createdAt,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                //trạng thái đơn hàng
                Surface(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(start = dimens.spaceSm),
                    shape = RoundedCornerShape(dimens.radiusSmall),
                    color = order.status.toColor()
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = dimens.spaceSm, vertical = 6.dp),
                        text = order.status.name,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = dimens.spaceXs, horizontal = dimens.spaceSm))


            //các sản phẩm trong đơn hàng
            order.items.forEachIndexed { idx, item ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = dimens.spaceSm, vertical = dimens.spaceXs),
                    verticalAlignment = Alignment.CenterVertically
                ){
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


                }
            }

            Divider(modifier = Modifier.padding(vertical = dimens.spaceXs, horizontal = dimens.spaceSm))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimens.spaceSm, vertical = dimens.spaceXs),
                verticalAlignment = Alignment.CenterVertically
                , horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Tổng: ${order.totalAmount.toDouble().toVNDString()}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f)
                )


                order.status.toPrimaryActionText()?.let { actionText ->
                    RoundedActionButton(
                        text = actionText,
                        onClick = onPrimaryAction,
                        horizontalPadding = dimens.spaceLg,
                        textStyle = MaterialTheme.typography.bodySmall,
                        cornerRadius = dimens.radiusMedium,
                        verticalPadding = 1.dp
                    )
                }


                Spacer(modifier = Modifier.width(dimens.spaceSm))
                RoundedActionButton(
                    text = "Xem chi tiết",
                    onClick = onOpenDetail,
                    horizontalPadding = dimens.spaceLg,
                    textStyle = MaterialTheme.typography.bodySmall
                )
            }


        }
    }
}

@Preview(showBackground = false)
@Composable
fun OrderCardPreview() {
    EasyMartTheme {
        OrderCard(
            order = mockOrders[0]
        )
    }
}