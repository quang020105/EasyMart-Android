package com.example.easymart.data.mapper

import com.example.easymart.data.local.entity.OrderEntity
import com.example.easymart.data.local.entity.OrderItemEntity
import com.example.easymart.data.local.relation.OrderWithItems
import com.example.easymart.data.remote.dto.OrderItemDto
import com.example.easymart.data.remote.dto.OrderRemoteDto
import com.example.easymart.data.remote.dto.OrderRemoteItemDto
import com.example.easymart.domain.model.CartItem
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderItem
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.Payment
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.Product

fun Order.toEntity(): OrderEntity {
    return OrderEntity(
        id = id,
        userId = userId,
        totalAmount = totalAmount,
        orderNumber = orderNumber,
        shippingAddress = shippingAddress.toEmbedded(),
        orderStatus = status,
        paymentStatus = paymentStatus,
        paymentMethod = paymentMethod,
        remoteId = remoteId,
        isSynced = isSynced,
        syncStatus = syncStatus,
        createdAt = createdAt,
        updatedAt = updatedAt
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
        paymentMethod = paymentMethod,
        paymentStatus = paymentStatus,
        shippingAddress = shippingAddress.toDomain(),
        createdAt = createdAt,
        updatedAt = updatedAt,
        remoteId = remoteId,
        isSynced = isSynced,
        syncStatus = syncStatus
    )
}


fun OrderItemEntity.toDomain(): OrderItem {
    return OrderItem(
        id = id,
        product = Product(
            id = this.productId,
            name = this.productName,
            description = "",
            price = this.price,
            imageUrl = this.productImage,
        ),
        quantity = quantity
    )
}

fun OrderItem.toEntity(): OrderItemEntity {
    return OrderItemEntity(
        orderId = id,
        productId = product.id,
        productName = product.name,
        productImage = product.imageUrl,
        price = product.price,
        quantity = quantity
    )
}

fun OrderWithItems.toDomain(): Order {
    val order = this.order
    val items = this.items.map { orderItemEntity -> orderItemEntity.toDomain() }
//    val paymentMethod = this.payment?.method
//    val paymentStatus = this.payment?.status
    return Order(
        id = order.id,
        userId = order.userId,
        orderNumber = "ORD-${order.id}",
        items = items,
        totalAmount = order.totalAmount,
        status = OrderStatus.valueOf(order.orderStatus.name),
        paymentStatus = order.paymentStatus,
        paymentMethod = order.paymentMethod,
        shippingAddress = order.shippingAddress.toDomain(),
        createdAt = order.createdAt,
        updatedAt = order.updatedAt,
        remoteId = order.remoteId,
        isSynced = order.isSynced,
        syncStatus = order.syncStatus
    )
}

fun OrderWithItems.toRemoteDto(): OrderRemoteDto {
    val order = this.order
    return OrderRemoteDto(
        localId = order.id,
        remoteId = order.remoteId,
        userId = order.userId,
        orderNumber = "ORD-${order.id}",
        totalAmount = order.totalAmount,
        orderStatus = order.orderStatus.name,
        paymentStatus = order.paymentStatus.name,
        paymentMethod = order.paymentMethod.name,
        shippingName = order.shippingAddress.name,
        shippingPhone = order.shippingAddress.phone,
        shippingAddressString = order.shippingAddress.addressString,
        createdAt = order.createdAt,
        updatedAt = order.updatedAt,
        items = items.map { it.toRemoteDto() }
    )
}

private fun OrderItemEntity.toRemoteDto(): OrderRemoteItemDto {
    return OrderRemoteItemDto(
        productId = productId,
        productName = productName,
        productImage = productImage,
        price = price,
        quantity = quantity
    )
}

fun OrderItem.toDto(): OrderItemDto {
    return OrderItemDto(
        productId = product.id,
        quantity = quantity,
        price = product.price
    )
}

fun OrderItemDto.toDomain(product: Product): OrderItem {
    return OrderItem(
        id = 0, // backend không quản lý id item local
        product = product,
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
