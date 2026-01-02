package com.example.easymart.presentation.ui.payment.mapper

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class PaymentMethodUiModel(
    @param:DrawableRes val iconRes: Int,
    @param:StringRes val titleRes: Int,
    @param:StringRes val descriptionRes: Int
)
