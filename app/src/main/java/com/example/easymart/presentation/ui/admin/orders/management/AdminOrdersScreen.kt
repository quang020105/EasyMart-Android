package com.example.easymart.presentation.ui.admin.orders.management
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.ui.admin.orders.management.components.AdminOrderCard
import com.example.easymart.presentation.ui.admin.orders.management.components.AdminOrderSearchBar
import com.example.easymart.presentation.ui.admin.orders.management.components.OrderFilterBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrdersScreen(
    orders: List<AdminOrdersUiModel>,
    modifier: Modifier = Modifier,
    searchQuery: String,
    selectedOrderStatus: OrderStatus?,
    selectedPaymentMethod: PaymentMethod?,
    selectedPaymentStatus: PaymentStatus?,
    onSearchQueryChange: (String) -> Unit,
    onOrderStatusSelected: (OrderStatus?) -> Unit,
    onPaymentMethodSelected: (PaymentMethod?) -> Unit,
    onPaymentStatusSelected: (PaymentStatus?) -> Unit,
    onOrderClick: (AdminOrdersUiModel) -> Unit
) {
    Surface {
        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                AdminOrderSearchBar(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChange,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OrderFilterBar(
                    selectedOrderStatus = selectedOrderStatus,
                    selectedPaymentMethod = selectedPaymentMethod,
                    selectedPaymentStatus = selectedPaymentStatus,
                    onOrderStatusSelected = onOrderStatusSelected,
                    onPaymentMethodSelected = onPaymentMethodSelected,
                    onPaymentStatusSelected = onPaymentStatusSelected
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.ui_text_009),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = stringResource(R.string.ui_text_061, orders.size),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF64748B)
                    )
                }
            }

            items(
                items = orders,
                key = { it.id }
            ) { order ->
                AdminOrderCard(
                    order = order,
                    onClick = { onOrderClick(order) }
                )
            }
        }
    }
}


//data class AdminOrderUiModel(
//    val id: String,
//    val customerName: String,
//    val phoneNumber: String,
//    val totalAmountText: String,
//    val orderStatus: OrderStatus,
//    val paymentMethod: PaymentMethod,
//    val paymentStatus: PaymentStatus,
//    val createdDateText: String
//)
@Preview
@Composable
private fun AdminOrderListScreenPreview() {
    EasyMartTheme {
        AdminOrdersScreen(
            orders = listOf(
                AdminOrdersUiModel(
                    id = "1",
                    customerName = "Nguyễn Văn A",
                    phoneNumber = "0123456789",
            totalAmountText = stringResource(R.string.ui_text_355),
                    orderStatus = OrderStatus.CONFIRMED,
                    paymentMethod = PaymentMethod.COD,
                    paymentStatus = PaymentStatus.UNPAID,
                    createdDateText = "01/01/2024"
                ),
                AdminOrdersUiModel(
                    id = "2",
                    customerName = "Trần Thị B",
                    phoneNumber = "0987654321",
            totalAmountText = stringResource(R.string.ui_text_356),
                    orderStatus = OrderStatus.DELIVERED,
                    paymentMethod = PaymentMethod.COD,
                    paymentStatus = PaymentStatus.PAID,
                    createdDateText = "02/01/2024"
                )
            ),
            searchQuery = "",
            selectedOrderStatus = null,
            selectedPaymentMethod = null,
            selectedPaymentStatus = null,
            onSearchQueryChange = {},
            onOrderStatusSelected = {},
            onPaymentMethodSelected = {},
            onPaymentStatusSelected = {},
            onOrderClick = {},
        )
    }
}
