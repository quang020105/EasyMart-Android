package com.example.easymart.utils

fun Double.toVNDString(): String {
    val vnd = this * 26333
    return "%,.0f ₫".format(vnd).replace(',', '.')
}