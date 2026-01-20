package com.example.easymart.utils

import com.example.easymart.domain.model.OrderStatus

fun Double.toVNDString(): String {
    val vnd = this * 26333
    return "%,.0f ₫".format(vnd).replace(',', '.')
}
