package com.example.easymart.presentation.ui.mock

import com.example.easymart.domain.model.Address
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderItem
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentStatus

val mockOrders = listOf(

    Order(
        id = 1,
        userId = 101,
        orderNumber = "ORD-001",
        items = listOf(
            OrderItem(
                id = 1,
                product = mockSimpleProduct,
                quantity = 1
            )
        ),
        totalAmount = 100_000,
        status = OrderStatus.DELIVERED,
        paymentStatus = PaymentStatus.UNPAID,
        paymentMethod = PaymentMethod.COD,
        shippingAddress = Address(
            name = "Nguyen Van A",
            phone = "0123456789",
            addressString = "Hà Nội"
        ),
        createdAt = System.currentTimeMillis()
    ),

    Order(
        id = 2,
        userId = 101,
        orderNumber = "ORD-002",
        items = listOf(
            OrderItem(
                id = 2,
                product = mockProducts[1],
                quantity = 2
            ),
            OrderItem(
                id = 3,
                product = mockProducts[2],
                quantity = 1
            )
        ),
        totalAmount = 2_500_000,
        status = OrderStatus.CONFIRMED,
        paymentStatus = PaymentStatus.SUCCESS,
        paymentMethod = PaymentMethod.ONLINE_GATEWAY,
        shippingAddress = Address(
            name = "Nguyen Van A",
            phone = "0123456789",
            addressString = "Hà Nội"
        ),
        createdAt = System.currentTimeMillis()
    ),

    Order(
        id = 3,
        userId = 101,
        orderNumber = "ORD-003",
        items = listOf(
            OrderItem(
                id = 4,
                product = mockProducts[3],
                quantity = 1
            )
        ),
        totalAmount = 1_200_000,
        status = OrderStatus.SHIPPING,
        paymentStatus = PaymentStatus.SUCCESS,
        paymentMethod = PaymentMethod.COD,
        shippingAddress = Address(
            name = "Trung Nguyen",
            phone = "0123456533",
            addressString = "Đà Nẵng"
        ),
        createdAt = System.currentTimeMillis()
    ),

    Order(
        id = 4,
        userId = 101,
        orderNumber = "ORD-004",
        items = listOf(
            OrderItem(
                id = 5,
                product = mockProducts[4],
                quantity = 3
            )
        ),
        totalAmount = 3_000_000,
        status = OrderStatus.DELIVERED,
        paymentStatus = PaymentStatus.SUCCESS,
        paymentMethod = PaymentMethod.ONLINE_GATEWAY,
        shippingAddress = Address(
            name = "Le Thi B",
            phone = "0987654321",
            addressString = "TP HCM"
        ),
        createdAt = System.currentTimeMillis()
    ),

    Order(
        id = 5,
        userId = 101,
        orderNumber = "ORD-005",
        items = listOf(
            OrderItem(
                id = 6,
                product = mockProducts[5],
                quantity = 1
            )
        ),
        totalAmount = 800_000,
        status = OrderStatus.CANCELLED,
        paymentStatus = PaymentStatus.UNPAID,
        paymentMethod = PaymentMethod.COD,
        shippingAddress = Address(
            name = "Pham Van C",
            phone = "0912345678",
            addressString = "Hải Phòng"
        ),
        createdAt = System.currentTimeMillis()
    )
)
