package com.example.easymart.presentation.ui.orderdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.mock.mockOrders
import com.example.easymart.presentation.ui.order.extension.toColor
import com.example.easymart.presentation.ui.orderdetail.components.OrderHeader
import com.example.easymart.presentation.ui.orderdetail.components.OrderPrimaryContent
import com.example.easymart.presentation.ui.orderdetail.components.ShippingInfo
import com.example.easymart.utils.toDisplayString

@Composable
fun OrderDetailScreen(
    uiState: OrderDetailUiState,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background
            )
    ) {


        when (uiState) {
            is OrderDetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Đang tải đơn hàng...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            is OrderDetailUiState.Error -> {
                val message = uiState.message
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimens.spaceMd),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                    //Spacer(modifier = Modifier.height(12.dp))
//                    Button(onClick = onRetry) {
//                        Text(text = "Thử lại")
//                    }
                }
            }


            is OrderDetailUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(dimens.spaceSm),
                    verticalArrangement = Arrangement.spacedBy(dimens.spaceMd)
                ) {
                    item {
                        OrderHeader(
                            orderId = uiState.order.id.toLong(),
                            createdAt = uiState.order.createdAt,
                            status = uiState.order.status
                        )
                    }
                    item {
                        ShippingInfo(
                            name = uiState.order.shippingAddress.name,
                            addressString = uiState.order.shippingAddress.addressString,
                            phone = uiState.order.shippingAddress.phone
                        )
                    }
                    item {
                        OrderPrimaryContent(
                            order = uiState.order
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun OrderDetailScreenPreview() {
    EasyMartTheme {
        OrderDetailScreen(
            uiState = OrderDetailUiState.Success(
                order = mockOrders[1]
            )
        )
    }
}