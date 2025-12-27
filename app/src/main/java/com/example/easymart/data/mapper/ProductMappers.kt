package com.example.easymart.data.mapper

import android.content.Context
import com.example.easymart.data.remote.dto.ProductDto
import com.example.easymart.domain.model.Product

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        name = title,
        description = description,
        category = category,
        price = price,
        imageUrl = image
    )
}