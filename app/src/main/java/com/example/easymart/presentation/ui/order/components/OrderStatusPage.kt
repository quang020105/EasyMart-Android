package com.example.easymart.presentation.ui.order.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.R
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderItem
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun ItemOrderStatusScreen(
    modifier: Modifier = Modifier,
    olderType: String, // Đơn hàng mới, Đang giao, Đã giao, Đã hủy
    order: Order
) {
    val dimens = LocalAppDimens.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ){
        LazyColumn (
            modifier = modifier.fillMaxSize().padding(all = dimens.spaceMd)
        ){
            items(order.items){ orderItem ->
                OrderCard(
                    orderItem = orderItem
                )
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun ItemOrderStatusScreenPreview() {
    EasyMartTheme {
        ItemOrderStatusScreen(
            olderType = "Đơn hàng mới",
            order = Order(
                id = 1,
                userId = 1,
                orderNumber = "ORD123456",
                totalAmount = 150.0,
                status = com.example.easymart.domain.model.OrderStatus.PENDING,
                shippingAddress = null,
                createdAt = "2023-10-01T10:00:00Z",
                payment = null,
                items = listOf(
                    OrderItem(
                        id = 1,
                        product = Product(
                            id = 1,
                            name = "Sample Product",
                            description = "This is a sample product.",
                            price = 50.0,
                            imageUrl = "https://via.placeholder.com/150",
                            imageRes = R.drawable.pic_shoe_1
                        ),
                        quantity = 2
                    ),
                    OrderItem(
                        id = 2,
                        product = Product(
                            id = 4,
                            name = "Nike Air Max 270",
                            description = "This is a limited shoe product.",
                            price = 8547.0,
                            imageUrl = "https://via.placeholder.com/150",
                            imageRes = R.drawable.pic_shoe_1
                        ),
                        quantity = 2
                    )
                )
            )
        )
    }
}