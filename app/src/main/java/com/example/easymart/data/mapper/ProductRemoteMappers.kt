package com.example.easymart.data.mapper

import com.example.easymart.data.local.entity.ProductEntity
import com.example.easymart.data.remote.dto.ProductFirestoreDto
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.model.ProductRating
import com.google.gson.Gson

fun ProductEntity.toRemoteDto(): ProductFirestoreDto {
    return ProductFirestoreDto(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl,
        imageUrls = decodeList(imageUrlsJson),
        brand = brand,
        category = category,
        ratingRate = ratingRate,
        ratingCount = ratingCount,
        stockQuantity = stockQuantity,
        isVisible = isVisible,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isDeleted = isDeleted,
        storagePath = storagePath
    )
}

fun ProductFirestoreDto.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl,
        imageUrlsJson = Gson().toJson(imageUrls),
        brand = brand,
        category = category,
        updatedAt = updatedAt,
        ratingRate = ratingRate,
        ratingCount = ratingCount,
        stockQuantity = stockQuantity,
        isVisible = isVisible,
        createdAt = createdAt,
        isDeleted = isDeleted,
        isSynced = true,
        storagePath = storagePath,
        localImageUri = null,
        localImageUrisJson = "[]"
    )
}

fun ProductFirestoreDto.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl,
        imageUrls = imageUrls,
        brand = brand,
        category = category,
        stockQuantity = stockQuantity,
        soldQuantity = 0, // API không cung cấp thông tin này
        rating = ProductRating(rate = ratingRate, count = ratingCount),
        isVisible = isVisible,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isDeleted = isDeleted,
        storagePath = storagePath
    )
}
