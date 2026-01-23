package com.example.easymart.data.mapper

import com.example.easymart.data.local.entity.CartItemEntity
import com.example.easymart.domain.model.CartItem
import com.example.easymart.domain.model.Product

fun CartItem.toEntity(cartId: String): CartItemEntity {
    return CartItemEntity(
        id = id,
        cartId = cartId,
        productId = product.id,
        name = product.name,
        price = product.price,
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
            price = price,
            imageUrl = imageUrl ?: "",
            description = ""
        ),
        quantity = quantity,
        price = price,
        addAt = addAt
    )
}