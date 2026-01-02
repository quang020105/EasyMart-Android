package com.example.easymart.data.mapper

import com.example.easymart.data.local.entity.OrderEntity
import com.example.easymart.data.local.entity.OrderItemEntity
import com.example.easymart.data.local.entity.OrderWithItems
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderItem
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.Payment
import com.example.easymart.domain.model.Product

fun Order.toEntity(): OrderEntity {
    return OrderEntity(
        id = id,
        userId = userId,
        totalAmount = totalAmount,
        orderNumber = orderNumber,
        shippingAddress = shippingAddress,
        orderStatus = status,
        createdAt = System.currentTimeMillis()
    )
}

fun OrderEntity.toDomain(
    items: List<OrderItem> = emptyList(),
    payment: Payment? = null
): Order {
    return Order(
        id = id,
        userId = userId,
        orderNumber = "ORD-$id",
        items = items,
        totalAmount = totalAmount,
        status = OrderStatus.valueOf(orderStatus.name),
        shippingAddress = shippingAddress,
        createdAt = createdAt.toString(),
        payment = payment
    )
}



fun OrderItemEntity.toDomain(product: Product): OrderItem {
    return OrderItem(
        id = id,
        product = product,
        quantity = quantity
    )
}

fun OrderItem.toEntity(): OrderItemEntity {
    return OrderItemEntity(
        orderId = id,
        productId = product.id.toString(),
        productName = product.name,
        productImage = product.imageUrl,
        price = product.price.toLong(),
        quantity = quantity
    )
}
