package com.example.easymart.presentation.ui.order

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.R
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderItem
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.ui.order.components.OrderStatusPage
import kotlinx.coroutines.launch


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OrderScreen(
    modifier: Modifier = Modifier,
    orders: List<Order> = emptyList(),
) {
    //danh sách tab
    val tabs = listOf(
        "Chờ xử lý" to OrderStatus.CREATED,
        "Đã xác nhận" to OrderStatus.CONFIRMED,
        "Đang giao" to OrderStatus.SHIPPED,
        "Đã giao" to OrderStatus.DELIVERED,
        "Đã hủy" to OrderStatus.CANCELLED
    )

    val pagerState = rememberPagerState(initialPage = 0) { tabs.size }
    val scope = rememberCoroutineScope()
    //giữ trạng thái tab được chọn
    val selectedIndex by derivedStateOf { pagerState.currentPage }

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
                    orders = listForPage.first().items,
                    olderType = tabs[pager].first
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

@Preview(showBackground = true)
@Composable
fun OrderScreenPreview() {
    EasyMartTheme {
        OrderScreen(
            orders = listOf(
                Order(
                    id = 1,
                    items = listOf(
                        OrderItem(
                            id = 1,
                            product = Product(
                                id = 4,
                                name = "Nike Air Max 270",
                                description = "This is a limited shoe product.",
                                price = 8547.0,
                                imageUrl = "https://via.placeholder.com/150",
                                imageRes = R.drawable.pic_shoe_1
                            ),
                            quantity = 2
                        ),
                        OrderItem(
                            id = 1,
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
                    ),
                    totalAmount = 130L,
                    status = OrderStatus.SHIPPED,
                    userId = 1,
                    orderNumber = "ruru"
                ),
                Order(
                    id = 2,
                    items = listOf(
                        OrderItem(
                            id = 1,
                            product = Product(
                                id = 4,
                                name = "Nike Air Max 270",
                                description = "This is a limited shoe product.",
                                price = 8547.0,
                                imageUrl = "https://via.placeholder.com/150",
                                imageRes = R.drawable.pic_shoe_1
                            ),
                            quantity = 2
                        ),
                        OrderItem(
                            id = 5,
                            product = Product(
                                id = 4,
                                name = "Nike Air Max 270",
                                description = "This is a limited shoe product.",
                                price = 8547.0,
                                imageUrl = "https://via.placeholder.com/150",
                                imageRes = R.drawable.pic_shoe_1
                            ),
                            quantity = 2
                        ),
                        OrderItem(
                            id = 4,
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
                    ),
                    totalAmount = 130L,
                    status = OrderStatus.DELIVERED,
                    userId = 1,
                    orderNumber = "ruru"
                )
            )
        )
    }
}