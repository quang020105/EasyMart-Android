package com.example.easymart.presentation.ui.payment.mapper


import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.R
object PaymentMethodUiMapper {
    fun map(method: PaymentMethod): PaymentMethodUiModel {
        return when (method) {
            PaymentMethod.COD -> PaymentMethodUiModel(
                iconRes = R.drawable.ic_shipping,
                titleRes = R.string.title_payment_cod,
                descriptionRes = R.string.decs_payment_cod
            )

            PaymentMethod.WALLET -> PaymentMethodUiModel(
                iconRes = R.drawable.ic_wallet,
                titleRes = R.string.title_payment_wallet,
                descriptionRes = R.string.desc_payment_wallet
            )

            PaymentMethod.ONLINE_GATEWAY -> PaymentMethodUiModel(
                iconRes = R.drawable.ic_credit_card,
                titleRes = R.string.title_payment_online,
                descriptionRes = R.string.desc_payment_online
            )
        }
    }
}