package com.example.easymart.data.mapper

import com.example.easymart.data.remote.dto.ImageSearchProductDto
import com.example.easymart.data.remote.dto.ImageSearchResponseDto
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.model.ProductRating
import com.example.easymart.domain.model.image_search.ImageSearchResult
import com.example.easymart.domain.model.image_search.SimilarProduct
import com.example.easymart.utils.legacyPriceToVnd

fun ImageSearchResponseDto.toDomain(): ImageSearchResult {
    val responseData = data

    return ImageSearchResult(
        confidence = responseData?.confidence ?: 0.0,
        products = responseData?.products.orEmpty().map { it.toDomain() }
    )
}

private fun ImageSearchProductDto.toDomain(): SimilarProduct {
    val normalizedName = name.ifBlank { title }
    val normalizedImageUrl = imageUrl.ifBlank { image }

    return SimilarProduct(
        product = Product(
            id = id,
            name = normalizedName,
            description = description,
            priceVnd = priceVnd.takeIf { it > 0L } ?: legacyPriceToVnd(id, price),
            imageUrl = normalizedImageUrl,
            imageUrls = imageUrls.filter { it.isNotBlank() },
            brand = brand,
            category = category,
            rating = ProductRating(
                rate = rating.rate,
                count = rating.count
            )
        ),
        score = score
    )
}
