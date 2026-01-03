package com.example.easymart.data.mapper

import com.example.easymart.data.local.entity.OrderEntity
import com.example.easymart.data.local.entity.OrderItemEntity
import com.example.easymart.data.local.entity.OrderWithItems
import com.example.easymart.domain.model.CartItem
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderItem
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.Payment
import com.example.easymart.domain.model.Product

fun Order.toEntity(): OrderEntity {
    return OrderEntity(
        userId = userId,
        totalAmount = totalAmount,
        orderNumber = orderNumber,
        shippingAddress = shippingAddress,
        orderStatus = status,
        paymentStatus = paymentStatus,
        createdAt = System.currentTimeMillis()
    )
}

fun OrderEntity.toDomain(
    items: List<OrderItem> = emptyList(),
): Order {
    return Order(
        id = id,
        userId = userId,
        orderNumber = "ORD-$id",
        items = items,
        totalAmount = totalAmount,
        status = OrderStatus.valueOf(orderStatus.name),
        paymentStatus = paymentStatus,
        shippingAddress = shippingAddress,
        createdAt = createdAt.toString(),
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

fun CartItem.cartToOrderItem(): OrderItem {
    return OrderItem(
        id = id,
        product = product,
        quantity = quantity
    )
}
