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
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.R
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderItem
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.mock.mockOrders

@Composable
fun OrderStatusPage(
    modifier: Modifier = Modifier,
    orders: List<Order> = emptyList(),
    onPrimaryAction: (order: Order) -> Unit = {},
    onSecondaryAction: () -> Unit = {},
    onViewDetail: (orderId: Int) -> Unit = {},
) {
    val dimens = LocalAppDimens.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(all = dimens.spaceMd)
        ) {
            items(orders) { order ->
                OrderCard(
                    order = order,
                    onPrimaryAction = onPrimaryAction,
                    onSecondaryAction = onSecondaryAction,
                    onOpenDetail  = onViewDetail
                )
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun OrderStatusPagePreview() {
    EasyMartTheme {
        OrderStatusPage(
            orders = mockOrders
        )
    }
}