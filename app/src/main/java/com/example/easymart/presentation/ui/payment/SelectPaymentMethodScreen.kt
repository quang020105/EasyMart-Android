package com.example.easymart.presentation.ui.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.common.components.RoundedActionButton
import com.example.easymart.presentation.ui.payment.components.PaymentMethodItem
import com.example.easymart.utils.toVNDString

@Composable
fun SelectPaymentMethodScreen(
    totalAmount: Double,
    uiState: PaymentUiState,
    onMethodSelected: (PaymentMethod) -> Unit,
    onConfirmClick: () -> Unit
) {
    val dimens = LocalAppDimens.current
    Box(
        modifier = Modifier
        .background(MaterialTheme.colorScheme.surface)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimens.spaceLg)
        ) {

            Text(
                text = "Tổng thanh toán",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = totalAmount.toVNDString(),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(dimens.spaceXl))

            Text(
                text = "Chọn phương thức thanh toán",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(dimens.spaceMd))

            PaymentMethodItem(
                method = PaymentMethod.ONLINE_GATEWAY,
                description = "Thẻ ngân hàng, VNPay, MoMo...",
                icon = Icons.Default.CreditCard,
                selected = uiState.selectedMethod == PaymentMethod.ONLINE_GATEWAY,
                onClick = { onMethodSelected(PaymentMethod.ONLINE_GATEWAY) }
            )

            PaymentMethodItem(
                method = PaymentMethod.COD,
                description = "Trả tiền mặt cho shipper",
                icon = Icons.Default.LocalShipping,
                selected = uiState.selectedMethod == PaymentMethod.COD,
                onClick = { onMethodSelected(PaymentMethod.COD) }
            )

            PaymentMethodItem(
                method = PaymentMethod.WALLET,
                description = "Số dư hiện tại: 500.000 đ",
                icon = Icons.Default.AccountBalanceWallet,
                selected = uiState.selectedMethod == PaymentMethod.WALLET,
                onClick = { onMethodSelected(PaymentMethod.WALLET) }
            )

            Spacer(Modifier.weight(1f))


            RoundedActionButton(
                text = "Xác nhận phương thức",
                onClick = onConfirmClick,
                verticalPadding = dimens.spaceLg,
                cornerRadius = dimens.radiusXl,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun SelectPaymentMethodScreenPreview(){
    EasyMartTheme {
        SelectPaymentMethodScreen(
            120000.0,
            PaymentUiState(selectedMethod = PaymentMethod.COD),
            onMethodSelected = {},
            onConfirmClick = {}
        )
    }
}
