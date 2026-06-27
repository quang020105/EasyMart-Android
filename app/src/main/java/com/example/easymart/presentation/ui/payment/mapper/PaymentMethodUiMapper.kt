package com.example.easymart.presentation.ui.payment.mapper


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.QrCode2
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.R
object PaymentMethodUiMapper {
    fun map(method: PaymentMethod): PaymentMethodUiModel {
        return when (method) {
            PaymentMethod.COD -> PaymentMethodUiModel(
                icon = Icons.Default.LocalShipping,
                titleRes = R.string.title_payment_cod,
                descriptionRes = R.string.decs_payment_cod
            )

            PaymentMethod.WALLET -> PaymentMethodUiModel(
                icon = Icons.Default.AccountBalanceWallet,
                titleRes = R.string.title_payment_wallet,
                descriptionRes = R.string.desc_payment_wallet
            )

            PaymentMethod.ONLINE_GATEWAY -> PaymentMethodUiModel(
                icon = Icons.Default.QrCode2,
                titleRes = R.string.title_payment_online,
                descriptionRes = R.string.desc_payment_online
            )
        }
    }
}