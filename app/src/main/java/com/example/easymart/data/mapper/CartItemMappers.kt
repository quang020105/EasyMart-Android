package com.example.easymart.data.mapper

import com.example.easymart.data.local.entity.CartItemEntity
import com.example.easymart.data.remote.dto.CartItemRemoteDto
import com.example.easymart.domain.model.CartItem
import com.example.easymart.domain.model.Product
import com.example.easymart.utils.legacyPriceToVnd

fun CartItem.toEntity(cartId: String): CartItemEntity {
    return CartItemEntity(
        id = id,
        cartId = cartId,
        productId = product.id,
        name = product.name,
        price = product.priceVnd.toDouble(),
        priceVnd = product.priceVnd,
        quantity = quantity,
        imageUrl = product.imageUrl,
    )
}

fun CartItemEntity.toDomain(): CartItem {
    return CartItem(
        id = id,
        product = Product(
            id = productId,
            name = name,
            priceVnd = priceVnd.takeIf { it > 0L } ?: legacyPriceToVnd(productId, price),
            imageUrl = imageUrl ?: "",
            description = ""
        ),
        quantity = quantity,
        unitPriceVnd = priceVnd.takeIf { it > 0L } ?: legacyPriceToVnd(productId, price),
        addAt = addAt
    )
}

fun CartItemEntity.toRemoteDto(updatedAt: Long = System.currentTimeMillis()): CartItemRemoteDto {
    return CartItemRemoteDto(
        productId = productId,
        name = name,
        price = priceVnd.toDouble(),
        priceVnd = priceVnd,
        currency = "VND",
        imageUrl = imageUrl,
        quantity = quantity,
        addAt = addAt,
        updatedAt = updatedAt
    )
}

fun CartItemRemoteDto.toEntity(cartId: String): CartItemEntity {
    return CartItemEntity(
        id = 0,
        cartId = cartId,
        productId = productId,
        name = name,
        price = (priceVnd.takeIf { it > 0L } ?: legacyPriceToVnd(productId, price)).toDouble(),
        priceVnd = priceVnd.takeIf { it > 0L } ?: legacyPriceToVnd(productId, price),
        imageUrl = imageUrl,
        quantity = quantity,
        addAt = addAt,
        isDeleted = false
    )
}
