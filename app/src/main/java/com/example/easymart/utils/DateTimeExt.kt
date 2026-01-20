package com.example.easymart.utils

import android.annotation.SuppressLint
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("NewApi")
fun Long.toDateTimeString(
    pattern: String = "dd/MM/yyyy HH:mm"
): String {
    val formatter = DateTimeFormatter
        .ofPattern(pattern, Locale.forLanguageTag("vi-VN"))
        .withZone(ZoneId.systemDefault())

    return formatter.format(Instant.ofEpochMilli(this))
}
