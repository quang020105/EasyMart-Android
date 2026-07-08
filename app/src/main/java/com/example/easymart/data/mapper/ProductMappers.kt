package com.example.easymart.data.mapper

import com.example.easymart.data.local.entity.ProductEntity
import com.example.easymart.data.remote.dto.ProductApiDto
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.model.ProductRating

fun ProductApiDto.toDomain(): Product {
    return Product(
        id = id,
        name = title,
        description = description,
        category = category,
        brand = "",
        price = price,
        imageUrl = image,
        rating = ProductRating(rate = rating.rate, count = rating.count)
    )
}

fun ProductApiDto.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        name = title,
        description = description,
        price = price,
        imageUrl = image,
        brand = "",
        category = category,
        updatedAt = System.currentTimeMillis(),
        ratingRate = rating.rate,
        ratingCount = rating.count,
        stockQuantity = 0,
        isVisible = true,
        createdAt = System.currentTimeMillis(),
        isDeleted = false,
        isSynced = true,
        storagePath = null,
        localImageUri = null,
        imageUrlsJson = "[]",
        localImageUrisJson = "[]"
    )
}

fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl,
        brand = brand,
        category = category,
        stockQuantity = stockQuantity,
        rating = ProductRating(rate = ratingRate, count = ratingCount),
        isVisible = isVisible,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isDeleted = isDeleted,
        isSynced = isSynced,
        storagePath = storagePath,
        localImageUri = localImageUri,
        imageUrls = decodeList(imageUrlsJson),
        localImageUris = decodeList(localImageUrisJson)
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl,
        brand = brand,
        category = category,
        updatedAt = updatedAt,
        ratingRate = rating.rate,
        ratingCount = rating.count,
        stockQuantity = stockQuantity,
        isVisible = isVisible,
        createdAt = createdAt,
        isDeleted = isDeleted,
        isSynced = isSynced,
        storagePath = storagePath,
        localImageUri = localImageUri,
        imageUrlsJson = encodeList(imageUrls),
        localImageUrisJson = encodeList(localImageUris)
    )
}
