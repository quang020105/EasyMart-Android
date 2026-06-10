package com.example.easymart.presentation.ui.admin.orders.management

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
fun AdminOrderListScreen(
    orders: List<AdminOrderUiModel>,
    modifier: Modifier = Modifier,
    searchQuery: String,
    selectedOrderStatus: OrderStatus?,
    selectedPaymentMethod: PaymentMethod?,
    selectedPaymentStatus: PaymentStatus?,
    onSearchQueryChange: (String) -> Unit,
    onOrderStatusSelected: (OrderStatus?) -> Unit,
    onPaymentMethodSelected: (PaymentMethod?) -> Unit,
    onPaymentStatusSelected: (PaymentStatus?) -> Unit,
    onOrderClick: (AdminOrderUiModel) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFF8FAFC),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Danh sách đơn hàng",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )

                        Text(
                            text = "Quản lý toàn bộ đơn hàng",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF64748B)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color(0xFF0F172A)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Rounded.FilterList,
                            contentDescription = "Bộ lọc",
                            tint = Color(0xFF2563EB)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF8FAFC)
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
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
                        text = "Đơn hàng",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "Hiển thị ${orders.size} đơn hàng",
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
        AdminOrderListScreen(
            orders = listOf(
                AdminOrderUiModel(
                    id = "1",
                    customerName = "Nguyễn Văn A",
                    phoneNumber = "0123456789",
                    totalAmountText = "1.000.000 đ",
                    orderStatus = OrderStatus.CONFIRMED,
                    paymentMethod = PaymentMethod.COD,
                    paymentStatus = PaymentStatus.UNPAID,
                    createdDateText = "01/01/2024"
                ),
                AdminOrderUiModel(
                    id = "2",
                    customerName = "Trần Thị B",
                    phoneNumber = "0987654321",
                    totalAmountText = "500.000 đ",
                    orderStatus = OrderStatus.DELIVERED,
                    paymentMethod = PaymentMethod.COD,
                    paymentStatus = PaymentStatus.SUCCESS,
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
            onNavigateBack = {}
        )
    }
}