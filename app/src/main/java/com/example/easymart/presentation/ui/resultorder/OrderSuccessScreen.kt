package com.example.easymart.presentation.ui.resultorder

import com.example.easymart.presentation.ui.resultorder.components.InfoRow
import com.example.easymart.presentation.ui.payment.mapper.PaymentMethodUiMapper


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.common.components.RoundedActionButton
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.presentation.theme.EasyMartTheme

@Composable
fun OrderSuccessScreen(
    orderId: Int,
    totalAmount: Long,
    paymentMethod: PaymentMethod,
    onViewOrderClick: () -> Unit,
    onContinueShoppingClick: () -> Unit
) {
    val dimens = LocalAppDimens.current
    val paymentUi = PaymentMethodUiMapper.map(paymentMethod)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(dimens.screenPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(dimens.space3xl))

        // ✅ ICON SUCCESS
        Box(
            modifier = Modifier
                .size(dimens.iconLarge * 2)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(dimens.iconLarge)
            )
        }

        Spacer(modifier = Modifier.height(dimens.spaceLg))

        // TITLE
        Text(
            text = "Đặt hàng thành công",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(dimens.spaceSm))

        Text(
            text = "Cảm ơn bạn đã mua sắm tại EasyMart",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(dimens.spaceXl))

        // 📦 ORDER INFO CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(dimens.radiusLarge),
            elevation = CardDefaults.cardElevation(dimens.cardElevation)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimens.spaceLg)
            ) {

                InfoRow(
                    label = "Mã đơn hàng",
                    value = "EM$orderId",
                    dimens = dimens
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = dimens.spaceMd),
                    thickness = dimens.dividerThickness
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = paymentUi.iconRes),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(dimens.iconMedium)
                    )

                    Spacer(modifier = Modifier.width(dimens.spaceMd))

                    Column {
                        Text(
                            text = stringResource(id = paymentUi.titleRes),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = stringResource(id = paymentUi.descriptionRes),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Divider(
                    modifier = Modifier.padding(vertical = dimens.spaceMd),
                    thickness = dimens.dividerThickness
                )

                InfoRow(
                    label = "Tổng thanh toán",
                    value = "${"%,d".format(totalAmount)} đ",
                    dimens = dimens,
                    valueColor = MaterialTheme.colorScheme.primary,
                    bold = true
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // ACTION BUTTONS
        RoundedActionButton(
            text = "Xem đơn hàng",
            onClick = onViewOrderClick,
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = dimens.radiusXl,
            verticalPadding = dimens.spaceMd
        )

        Spacer(modifier = Modifier.height(dimens.spaceSm))

        OutlinedButton(
            onClick = onContinueShoppingClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimens.buttonHeight),
            shape = RoundedCornerShape(dimens.radiusXl)
        ) {
            Text(text = "Tiếp tục mua sắm")
        }

        Spacer(modifier = Modifier.height(dimens.spaceLg))
    }
}

@Preview
@Composable
fun OrderSuccessScreenPreview() {
    EasyMartTheme {
        OrderSuccessScreen(
            orderId = 123456789,
            totalAmount = 1500000,
            paymentMethod = PaymentMethod.COD,
            onViewOrderClick = {},
            onContinueShoppingClick = {},
        )
    }
}
