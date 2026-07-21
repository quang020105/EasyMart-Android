package com.example.easymart.data.mapper

import com.example.easymart.data.local.entity.AddressEmbedded
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
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.model.RefundMode
import com.example.easymart.domain.model.SyncStatus
import com.example.easymart.utils.DEFAULT_SHIPPING_FEE_PER_ITEM_VND
import com.example.easymart.utils.legacyPriceToVnd

fun Order.toEntity(): OrderEntity {
    return OrderEntity(
        id = id,
        userId = userId,
        totalAmount = totalAmount,
        subtotal = subtotal,
        shippingFee = shippingFee,
        orderNumber = orderNumber,
        shippingAddress = shippingAddress.toEmbedded(),
        orderStatus = status,
        paymentStatus = paymentStatus,
        paymentMethod = paymentMethod,
        remoteId = remoteId,
        isSynced = isSynced,
        syncStatus = syncStatus,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deliveredAt = deliveredAt,
        stockDeducted = stockDeducted,
        stockRestored = stockRestored,
        cancellationReason = cancellationReason,
        cancellationRequestedAt = cancellationRequestedAt,
        cancellationRequestedBy = cancellationRequestedBy,
        cancelledAt = cancelledAt,
        cancelledBy = cancelledBy,
        refundAmountVnd = refundAmountVnd,
        refundMode = refundMode,
        refundedAt = refundedAt,
        refundedBy = refundedBy
    )
}

fun OrderEntity.toDomain(
    items: List<OrderItem> = emptyList(),
): Order {
    return Order(
        id = id,
        userId = userId,
        orderNumber = orderNumber,
        items = items,
        totalAmount = totalAmount,
        subtotal = subtotal,
        shippingFee = shippingFee,
        status = OrderStatus.valueOf(orderStatus.name),
        paymentMethod = paymentMethod,
        paymentStatus = paymentStatus,
        shippingAddress = shippingAddress.toDomain(),
        createdAt = createdAt,
        updatedAt = updatedAt,
        deliveredAt = deliveredAt,
        remoteId = remoteId,
        isSynced = isSynced,
        syncStatus = syncStatus,
        stockDeducted = stockDeducted,
        stockRestored = stockRestored,
        cancellationReason = cancellationReason,
        cancellationRequestedAt = cancellationRequestedAt,
        cancellationRequestedBy = cancellationRequestedBy,
        cancelledAt = cancelledAt,
        cancelledBy = cancelledBy,
        refundAmountVnd = refundAmountVnd,
        refundMode = refundMode,
        refundedAt = refundedAt,
        refundedBy = refundedBy
    )
}


fun OrderItemEntity.toDomain(): OrderItem {
    return OrderItem(
        id = id,
        product = Product(
            id = this.productId,
            name = this.productName,
            description = "",
            priceVnd = priceVnd.takeIf { it > 0L } ?: legacyPriceToVnd(productId, price),
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
        price = product.priceVnd.toDouble(),
        priceVnd = product.priceVnd,
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
        orderNumber = order.orderNumber,
        items = items,
        totalAmount = order.totalAmount,
        subtotal = order.subtotal,
        shippingFee = order.shippingFee,
        status = OrderStatus.valueOf(order.orderStatus.name),
        paymentStatus = order.paymentStatus,
        paymentMethod = order.paymentMethod,
        shippingAddress = order.shippingAddress.toDomain(),
        createdAt = order.createdAt,
        updatedAt = order.updatedAt,
        deliveredAt = order.deliveredAt,
        remoteId = order.remoteId,
        isSynced = order.isSynced,
        syncStatus = order.syncStatus,
        stockDeducted = order.stockDeducted,
        stockRestored = order.stockRestored,
        cancellationReason = order.cancellationReason,
        cancellationRequestedAt = order.cancellationRequestedAt,
        cancellationRequestedBy = order.cancellationRequestedBy,
        cancelledAt = order.cancelledAt,
        cancelledBy = order.cancelledBy,
        refundAmountVnd = order.refundAmountVnd,
        refundMode = order.refundMode,
        refundedAt = order.refundedAt,
        refundedBy = order.refundedBy
    )
}

fun OrderWithItems.toRemoteDto(): OrderRemoteDto {
    val order = this.order
    return OrderRemoteDto(
        localId = order.id,
        remoteId = order.remoteId,
        userId = order.userId,
        orderNumber = order.orderNumber.ifBlank { "ORD-${order.id}" },
        totalAmount = order.totalAmount,
        subtotal = order.subtotal,
        shippingFee = order.shippingFee,
        currency = "VND",
        moneySchemaVersion = 2,
        orderStatus = order.orderStatus.name,
        paymentStatus = order.paymentStatus.name,
        paymentMethod = order.paymentMethod.name,
        shippingName = order.shippingAddress.name,
        shippingPhone = order.shippingAddress.phone,
        shippingAddressString = order.shippingAddress.addressString,
        createdAt = order.createdAt,
        updatedAt = order.updatedAt,
        deliveredAt = order.deliveredAt,
        stockDeducted = order.stockDeducted,
        stockRestored = order.stockRestored,
        cancellationReason = order.cancellationReason,
        cancellationRequestedAt = order.cancellationRequestedAt,
        cancellationRequestedBy = order.cancellationRequestedBy,
        cancelledAt = order.cancelledAt,
        cancelledBy = order.cancelledBy,
        refundAmountVnd = order.refundAmountVnd,
        refundMode = order.refundMode?.name,
        refundedAt = order.refundedAt,
        refundedBy = order.refundedBy,
        items = items.map { it.toRemoteDto() }
    )
}

fun OrderRemoteDto.toEntity(existingLocalId: Int = 0): OrderEntity {
    return OrderEntity(
        id = existingLocalId,
        userId = userId,
        orderNumber = orderNumber.ifBlank {
            if (localId > 0) "ORD-$localId" else remoteId.orEmpty()
        },
        totalAmount = totalAmount,
        subtotal = subtotal.takeIf { it > 0L } ?: totalAmount,
        shippingFee = shippingFee,
        orderStatus = parseOrderStatus(orderStatus),
        paymentStatus = parsePaymentStatus(paymentStatus),
        paymentMethod = parsePaymentMethod(paymentMethod),
        shippingAddress = AddressEmbedded(
            name = shippingName,
            phone = shippingPhone,
            addressString = shippingAddressString
        ),
        remoteId = remoteId,
        isSynced = true,
        syncStatus = SyncStatus.SYNCED,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deliveredAt = deliveredAt,
        stockDeducted = stockDeducted,
        stockRestored = stockRestored,
        cancellationReason = cancellationReason,
        cancellationRequestedAt = cancellationRequestedAt,
        cancellationRequestedBy = cancellationRequestedBy,
        cancelledAt = cancelledAt,
        cancelledBy = cancelledBy,
        refundAmountVnd = refundAmountVnd,
        refundMode = refundMode?.let { parseRefundMode(it) },
        refundedAt = refundedAt,
        refundedBy = refundedBy
    )
}

fun OrderRemoteDto.toDomainOrder(): Order {
    return toEntity(existingLocalId = localId).toDomain(
        items = items.map { it.toEntity().toDomain() }
    )
}

fun OrderRemoteDto.toVndNormalized(): OrderRemoteDto {
    if (items.isEmpty()) return this

    val normalizedItems = items.map { item ->
        val unitPriceVnd = item.priceVnd.takeIf { it > 0L }
            ?: legacyPriceToVnd(item.productId, item.price)
        item.copy(price = unitPriceVnd.toDouble(), priceVnd = unitPriceVnd)
    }

    if (currency == "VND" && moneySchemaVersion >= 2 && normalizedItems == items) {
        return this
    }

    val subtotalVnd = normalizedItems.sumOf { it.priceVnd * it.quantity }
    val shippingFeeVnd = normalizedItems.sumOf { it.quantity } * DEFAULT_SHIPPING_FEE_PER_ITEM_VND
    return copy(
        items = normalizedItems,
        subtotal = subtotalVnd,
        shippingFee = shippingFeeVnd,
        totalAmount = subtotalVnd + shippingFeeVnd,
        currency = "VND",
        moneySchemaVersion = 2
    )
}

fun OrderRemoteItemDto.toEntity(orderId: Int = 0): OrderItemEntity {
    return OrderItemEntity(
        orderId = orderId,
        productId = productId,
        productName = productName,
        productImage = productImage,
        price = priceVnd.toDouble(),
        priceVnd = priceVnd,
        quantity = quantity
    )
}

private fun OrderItemEntity.toRemoteDto(): OrderRemoteItemDto {
    return OrderRemoteItemDto(
        productId = productId,
        productName = productName,
        productImage = productImage,
        price = priceVnd.toDouble(),
        priceVnd = priceVnd,
        quantity = quantity
    )
}

private fun parseOrderStatus(value: String): OrderStatus =
    runCatching { OrderStatus.valueOf(value) }.getOrDefault(OrderStatus.CREATED)

private fun parsePaymentStatus(value: String): PaymentStatus =
    runCatching { PaymentStatus.valueOf(value) }.getOrDefault(PaymentStatus.UNPAID)

private fun parsePaymentMethod(value: String): PaymentMethod =
    runCatching { PaymentMethod.valueOf(value) }.getOrDefault(PaymentMethod.COD)

private fun parseRefundMode(value: String): RefundMode? =
    runCatching { RefundMode.valueOf(value) }.getOrNull()

fun OrderItem.toDto(): OrderItemDto {
    return OrderItemDto(
        productId = product.id,
        quantity = quantity,
        priceVnd = product.priceVnd
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
