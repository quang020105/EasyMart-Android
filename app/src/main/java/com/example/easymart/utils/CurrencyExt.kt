package com.example.easymart.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

const val VND_PER_USD = 26_333L
const val DEFAULT_SHIPPING_FEE_PER_ITEM_VND = 15_000L

fun Long.toVNDString(): String =
    "${NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(this)} VND"

// Converts FakeStore's USD price at the import boundary only
fun Double.usdToVnd(): Long =
    BigDecimal.valueOf(this)
        .multiply(BigDecimal.valueOf(VND_PER_USD))
        .setScale(0, RoundingMode.HALF_UP)
        .longValueExact()

fun legacyPriceToVnd(productId: Int, legacyPrice: Double): Long =
    if (productId in 1..20) legacyPrice.usdToVnd() else legacyPrice.toLong()
