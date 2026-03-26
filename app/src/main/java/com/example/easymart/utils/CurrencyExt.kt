package com.example.easymart.utils

import com.example.easymart.domain.model.OrderStatus

fun Double.toVNDString(): String {
    val vnd = this * 26333
    return "%,.0f ₫".format(vnd).replace(',', '.')
}

fun Double.toVNDLong(): Long {
    return (this * 26.333).toLong() // giảm số lượng số 0 ở cuối để thanh toán khả thi
}

fun Long.toVNDLong(): Long {
    return (this * 26.333).toLong() // giảm số lượng số 0 ở cuối để thanh toán khả thi
}


