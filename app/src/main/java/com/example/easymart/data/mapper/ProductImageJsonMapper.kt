package com.example.easymart.data.mapper

import com.google.gson.Gson

private val productImagesGson = Gson()

// chuyển list string thành json string để lưu vào database
fun encodeList(values: List<String>): String = productImagesGson.toJson(values)

// chuyển json string thành list string để sử dụng trong app
fun decodeList(raw: String?): List<String> =
    runCatching {
        productImagesGson
            .fromJson(raw ?: "[]", Array<String>::class.java)
            .toList()
    }.getOrElse { emptyList() }