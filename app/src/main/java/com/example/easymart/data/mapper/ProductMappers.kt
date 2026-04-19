package com.example.easymart.data.mapper

import com.example.easymart.data.local.entity.ProductEntity
import com.example.easymart.data.remote.dto.ProductDto
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.model.ProductRating

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        name = title,
        description = description,
        category = category,
        price = price,
        imageUrl = image,
        rating = ProductRating(rate = rating.rate, count = rating.count)
    )
}

fun ProductDto.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        name = title,
        description = description,
        price = price,
        imageUrl = image,
        category = category,
        updatedAt = System.currentTimeMillis(),
        ratingRate = rating.rate,
        ratingCount = rating.count
    )
}

fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl,
        category = category,
        rating = ProductRating(rate = ratingRate, count = ratingCount)
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl,
        category = category,
        updatedAt = System.currentTimeMillis(),
        ratingRate = rating.rate,
        ratingCount = rating.count
    )
}
