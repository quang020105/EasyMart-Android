package com.example.easymart.presentation.ui.order

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.ui.mock.mockOrders
import com.example.easymart.presentation.ui.order.components.OrderStatusPage
import kotlinx.coroutines.launch


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OrderScreen(
    modifier: Modifier = Modifier,
    uiState: OrderListUiState,
    onPrimaryAction: (order: Order) -> Unit = {},
    onSecondaryAction: () -> Unit = {},
    onViewDetail: (orderId: Int) -> Unit = {},
    onRetry: () -> Unit = {}
) {
    //danh sách tab
    val tabs = listOf(
        "Chờ xác nhận" to OrderStatus.CREATED,
        "Đã xác nhận" to OrderStatus.CONFIRMED,
        "Chờ lấy hàng" to OrderStatus.PACKING,
        "Đang giao" to OrderStatus.SHIPPING,
        "Đã giao" to OrderStatus.DELIVERED,
        "Đã hủy" to OrderStatus.CANCELLED
    )

    val pagerState = rememberPagerState(initialPage = 0) { tabs.size }
    val scope = rememberCoroutineScope()
    //giữ trạng thái tab được chọn
    val selectedIndex by derivedStateOf { pagerState.currentPage }


    when(uiState) {
        is OrderListUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is OrderListUiState.Error -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = uiState.message.ifBlank { "Không thể tải danh sách đơn hàng" },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(onClick = onRetry) {
                        Text(text = "Thử lại")
                    }
                }
            }
        }
        is OrderListUiState.Empty -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Bạn chưa có đơn hàng nào",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }
        }
        is OrderListUiState.Success -> {
            val orders = uiState.orders
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                ScrollableTabRow(
                    selectedTabIndex = selectedIndex,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    tabs.forEachIndexed { index, pair ->
                        val (title, _) = pair
                        Tab(
                            selected = selectedIndex == index,
                            onClick = {
                                scope.launch { pagerState.animateScrollToPage(index) }
                            },
                            text = { Text(text = title) }
                        )
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) {pager ->
                    val status = tabs[pager].second
                    val listForPage = orders.filter { it.status == status }
                    if(listForPage.isNotEmpty()){
                        OrderStatusPage(
                            orders = listForPage,
                            onPrimaryAction = onPrimaryAction,
                            onSecondaryAction = onSecondaryAction,
                            onViewDetail = onViewDetail
                        )
                    } else {
                        Text(
                            text = "Không có đơn hàng nào",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.background)
                                .wrapContentHeight(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun OrderScreenPreview() {
    EasyMartTheme {
        OrderScreen(
            uiState = OrderListUiState.Success(
                orders = mockOrders
            )
        )
    }
}