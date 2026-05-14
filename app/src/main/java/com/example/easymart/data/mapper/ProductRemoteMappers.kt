package com.example.easymart.data.mapper

import com.example.easymart.data.local.entity.ProductEntity
import com.example.easymart.data.remote.dto.ProductFirestoreDto
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.model.ProductRating

fun ProductEntity.toRemoteDto(): ProductFirestoreDto {
    return ProductFirestoreDto(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl,
        category = category,
        ratingRate = ratingRate,
        ratingCount = ratingCount,
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
        category = category,
        updatedAt = updatedAt,
        ratingRate = ratingRate,
        ratingCount = ratingCount,
        isVisible = isVisible,
        createdAt = createdAt,
        isDeleted = isDeleted,
        isSynced = true,
        storagePath = storagePath,
        localImageUri = null
    )
}

fun ProductFirestoreDto.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl,
        category = category,
        stockQuantity = 0, // API không cung cấp thông tin này
        soldQuantity = 0, // API không cung cấp thông tin này
        rating = ProductRating(rate = ratingRate, count = ratingCount),
        isVisible = isVisible,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isDeleted = isDeleted,
        storagePath = storagePath
    )
}

