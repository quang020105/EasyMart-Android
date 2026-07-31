package com.example.easymart.presentation.ui.order.components
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.common.components.ProductCard
import com.example.easymart.presentation.ui.common.components.RoundedActionButton
import com.example.easymart.presentation.ui.mock.mockOrders
import com.example.easymart.presentation.ui.order.extension.toColor
import com.example.easymart.presentation.ui.order.extension.toPrimaryActionText
import com.example.easymart.utils.colorScheme
import com.example.easymart.utils.toDateTimeString
import com.example.easymart.utils.toDisplayString
import com.example.easymart.utils.toVNDString

@Composable
fun OrderCard(
    modifier: Modifier = Modifier,
    order: Order,
    onPrimaryAction: (order: Order) -> Unit = {},
    onSecondaryAction: () -> Unit = {},
    onOpenDetail: (orderId: Int) -> Unit = {}
) {
    val dimens = LocalAppDimens.current

    val displayOrderCode = order.orderNumber.ifBlank {
        order.remoteId ?: "ORD-${order.id}"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = dimens.spaceXs)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                shape = RoundedCornerShape(dimens.radiusMedium)
            ),
        shape = RoundedCornerShape(dimens.radiusMedium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimens.spaceMd,
                        top = dimens.spaceMd,
                        end = dimens.spaceMd,
                        bottom = dimens.spaceSm
                    ),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = displayOrderCode,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = order.createdAt.toDateTimeString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PaymentStatusChip(
                        status = order.paymentStatus
                    )
                }

                Spacer(modifier = Modifier.width(dimens.spaceSm))

                OrderStatusChip(
                    statusText = order.status.toDisplayString(),
                    containerColor = order.status.toColor()
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(
                    horizontal = dimens.spaceMd,
                    vertical = dimens.spaceXs
                ),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
            )

            order.items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = dimens.spaceMd,
                            vertical = dimens.spaceXs
                        ),
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

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = item.product.name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = stringResource(R.string.ui_text_239, item.quantity),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(dimens.spaceSm))

                    Text(
                        text = item.totalPriceVnd.toVNDString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(
                    horizontal = dimens.spaceMd,
                    vertical = dimens.spaceXs
                ),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimens.spaceMd,
                        top = dimens.spaceXs,
                        end = dimens.spaceMd,
                        bottom = dimens.spaceMd
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.ui_text_207),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = order.totalAmount.toVNDString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }

                order.status
                    .toPrimaryActionText()
                    ?.let { actionText ->
                        RoundedActionButton(
                            text = actionText,
                            onClick = { onPrimaryAction(order) },
                            horizontalPadding = dimens.spaceMd,
                            textStyle = MaterialTheme.typography.bodySmall,
                            cornerRadius = dimens.radiusMedium,
                            verticalPadding = 4.dp
                        )

                        Spacer(modifier = Modifier.width(dimens.spaceSm))
                    }

                RoundedActionButton(
                    text = stringResource(R.string.ui_text_300),
                    onClick = { onOpenDetail(order.id) },
                    horizontalPadding = dimens.spaceMd,
                    textStyle = MaterialTheme.typography.bodySmall,
                    cornerRadius = dimens.radiusMedium,
                    verticalPadding = 4.dp
                )
            }
        }
    }
}

@Composable
private fun OrderStatusChip(
    statusText: String,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.wrapContentWidth(),
        shape = RoundedCornerShape(999.dp),
        color = containerColor
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            text = statusText,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}

@Composable
private fun PaymentStatusChip(
    status: PaymentStatus,
    modifier: Modifier = Modifier
) {
    val chipColorScheme = status.colorScheme()
    val chipShape = RoundedCornerShape(999.dp)

    Surface(
        modifier = modifier
            .wrapContentWidth()
            .border(
                width = 1.dp,
                color = chipColorScheme.borderColor,
                shape = chipShape
            ),
        shape = chipShape,
        color = chipColorScheme.containerColor
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            text = status.toDisplayString(),
            style = MaterialTheme.typography.labelMedium,
            color = chipColorScheme.contentColor,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
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
