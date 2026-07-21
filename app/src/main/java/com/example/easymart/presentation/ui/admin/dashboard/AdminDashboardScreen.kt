package com.example.easymart.presentation.ui.admin.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.R
import com.example.easymart.domain.model.DashboardPeriod
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.admin.dashboard.components.AnalyticsSummaryRow
import com.example.easymart.presentation.ui.admin.dashboard.components.DashboardStatCard
import com.example.easymart.presentation.ui.admin.dashboard.components.PeriodChip
import com.example.easymart.presentation.ui.admin.dashboard.components.QuickActionCard
import com.example.easymart.presentation.ui.admin.dashboard.components.WelcomeBanner
import kotlin.math.roundToInt

@Composable
fun AdminDashboardScreen(
    uiState: AdminDashboardUiState,
    onRefresh: () -> Unit,
    onSelectPeriod: (DashboardPeriod) -> Unit,
    onImportFakeStoreProducts: () -> Unit,
    onNavigateOrders: () -> Unit,
    onNavigateProducts: () -> Unit,
    onNavigateCategories: () -> Unit
) {
    val dimens = LocalAppDimens.current
    val listState = rememberLazyListState()
    var isPeriodMenuVisible by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(dimens.screenPadding),
            verticalArrangement = Arrangement.spacedBy(dimens.spaceMd)
        ) {
            item {
                WelcomeBanner(
                    userName = "Admin",
                    subtitle = "Đây là tổng quan hoạt động của cửa hàng hôm nay.",
                    illustrationPainter = painterResource(id = R.drawable.ic_shop)
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimens.spaceMd)
                ) {
                    DashboardStatCard(
                        title = "Doanh thu",
                        value = formatCurrency(uiState.revenueVnd),
                        trendText = formatTrend(uiState.revenueVnd, uiState.previousRevenueVnd),
                        tintColor = Color(0xFFF1F7FF),
                        iconBackground = Color(0xFFE3F0FF),
                        valueColor = Color(0xFF2563EB),
                        trendColor = Color(0xFF16A34A),
                        topIcon = Icons.Filled.AccountBalanceWallet,
                        topRightIcon = Icons.Filled.BarChart,
                        modifier = Modifier.weight(1f)
                    )

                    DashboardStatCard(
                        title = "Đơn mới",
                        value = uiState.ordersInPeriod.toString(),
                        trendText = formatTrend(
                            uiState.ordersInPeriod.toLong(),
                            uiState.previousOrdersInPeriod.toLong()
                        ),
                        tintColor = Color(0xFFF0FDF4),
                        iconBackground = Color(0xFFDCFCE7),
                        valueColor = Color(0xFF16A34A),
                        trendColor = Color(0xFF16A34A),
                        topIcon = Icons.Filled.ShoppingCart,
                        topRightIcon = Icons.AutoMirrored.Filled.TrendingUp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Text(
                    text = "Chức năng nhanh",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(dimens.spaceSm)
                ) {
                    QuickActionCard(
                        title = "Đơn hàng",
                        subtitle = "Quản lý đơn hàng",
                        icon = Icons.Filled.Description,
                        iconTint = Color(0xFF6D28D9),
                        iconContainerColor = Color(0xFFF3ECFF),
                        onClick = onNavigateOrders
                    )
                    QuickActionCard(
                        title = "Sản phẩm",
                        subtitle = "Quản lý sản phẩm",
                        icon = Icons.Filled.Inventory2,
                        iconTint = Color(0xFF2563EB),
                        iconContainerColor = Color(0xFFE8F0FF),
                        onClick = onNavigateProducts
                    )
                    QuickActionCard(
                        title = "Danh mục",
                        subtitle = "Quản lý danh mục",
                        icon = Icons.Filled.GridView,
                        iconTint = Color(0xFFEA580C),
                        iconContainerColor = Color(0xFFFFEDD5),
                        onClick = onNavigateCategories
                    )
                    QuickActionCard(
                        title = "Import FakeStore",
                        subtitle = if (uiState.isImportingProducts) {
                            "Đang đồng bộ lên Firebase..."
                        } else {
                            "Đưa sản phẩm mẫu lên Firebase"
                        },
                        icon = Icons.Filled.CloudDownload,
                        iconTint = Color(0xFF0891B2),
                        iconContainerColor = Color(0xFFE0F7FA),
                        onClick = onImportFakeStoreProducts
                    )
                }
            }

            item {
                if (uiState.isImportingProducts || uiState.importMessage != null) {
                    ImportStatusCard(
                        isLoading = uiState.isImportingProducts,
                        message = uiState.importMessage ?: "Đang import sản phẩm FakeStore..."
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Thống kê nhanh",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Column(horizontalAlignment = Alignment.End) {
                                PeriodChip(
                                    text = uiState.period.label,
                                    onClick = { isPeriodMenuVisible = true }
                                )
                                DropdownMenu(
                                    expanded = isPeriodMenuVisible,
                                    onDismissRequest = { isPeriodMenuVisible = false }
                                ) {
                                    DashboardPeriod.entries.forEach { period ->
                                        DropdownMenuItem(
                                            text = { Text(period.label) },
                                            onClick = {
                                                isPeriodMenuVisible = false
                                                onSelectPeriod(period)
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(dimens.spaceMd))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column {
                                AnalyticsSummaryRow(
                                    title = "Sản phẩm",
                                    subtitle = "Tổng số sản phẩm trong cửa hàng",
                                    value = uiState.productCount.toString(),
                                    icon = Icons.Filled.Inventory2,
                                    iconTint = Color(0xFF2563EB),
                                    iconContainerColor = Color(0xFFE8F0FF),
                                    valueColor = Color(0xFF111827),
                                    sparklineColor = Color(0xFF60A5FA),
                                    sparklineValues = null
                                )
                                HorizontalDivider(color = Color(0xFFF1F5F9))
                                AnalyticsSummaryRow(
                                    title = "Danh mục",
                                    subtitle = "Tổng số danh mục",
                                    value = uiState.categoryCount.toString(),
                                    icon = Icons.Filled.GridView,
                                    iconTint = Color(0xFF7C3AED),
                                    iconContainerColor = Color(0xFFF3ECFF),
                                    valueColor = Color(0xFF111827),
                                    sparklineColor = Color(0xFFA78BFA),
                                    sparklineValues = null
                                )
                                HorizontalDivider(color = Color(0xFFF1F5F9))
                                AnalyticsSummaryRow(
                                    title = "Đơn hàng",
                                    subtitle = "Tổng số đơn hàng",
                                    value = uiState.totalOrderCount.toString(),
                                    icon = Icons.Filled.ShoppingCart,
                                    iconTint = Color(0xFF16A34A),
                                    iconContainerColor = Color(0xFFDCFCE7),
                                    valueColor = Color(0xFF111827),
                                    sparklineColor = Color(0xFF34D399),
                                    sparklineValues = uiState.dailyOrders.map { it.value.toFloat() }
                                )
                                HorizontalDivider(color = Color(0xFFF1F5F9))
                                AnalyticsSummaryRow(
                                    title = "Cần xử lý",
                                    subtitle = "Đơn chờ xác nhận hoặc chờ duyệt hủy",
                                    value = uiState.pendingActionCount.toString(),
                                    icon = Icons.Filled.Description,
                                    iconTint = Color(0xFFEA580C),
                                    iconContainerColor = Color(0xFFFFEDD5),
                                    valueColor = Color(0xFF111827),
                                    sparklineColor = Color(0xFFFB923C),
                                    sparklineValues = null
                                )
                            }
                        }
                    }
                }
            }

            item {
                if (uiState.isLoading) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (uiState.error != null) {
                    ErrorCard(message = uiState.error, onRetry = onRefresh)
                }
            }
        }
    }
}

@Composable
private fun ErrorCard(message: String, onRetry: () -> Unit) {
    val dimens = LocalAppDimens.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.spaceMd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.weight(1f)
            )
            FilledTonalButton(onClick = onRetry) {
                Text(text = "Thử lại")
            }
        }
    }
}

@Composable
private fun ImportStatusCard(
    isLoading: Boolean,
    message: String
) {
    val dimens = LocalAppDimens.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.spaceMd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp))
            }
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF1E3A8A)
            )
        }
    }
}

private fun formatCurrency(value: Long): String {
    return "${"%,d".format(value)} đ"
}

private fun formatTrend(current: Long, previous: Long): String {
    if (previous == 0L) {
        return if (current == 0L) "Chưa có dữ liệu so sánh" else "Mới trong kỳ này"
    }

    val percentage = ((current - previous).toDouble() / previous.toDouble() * 100).roundToInt()
    return when {
        percentage > 0 -> "Tăng $percentage% so với kỳ trước"
        percentage < 0 -> "Giảm ${-percentage}% so với kỳ trước"
        else -> "Không đổi so với kỳ trước"
    }
}


@Preview
@Composable
fun AdminDashboardPreview() {
    EasyMartTheme {
        AdminDashboardScreen(
            uiState = AdminDashboardUiState(
                revenueVnd = 12500000L,
                ordersInPeriod = 320,
                totalOrderCount = 320,
                productCount = 120,
                categoryCount = 8,
                isLoading = false,
                error = null
            ),
            onRefresh = {},
            onSelectPeriod = {},
            onImportFakeStoreProducts = {},
            onNavigateOrders = {},
            onNavigateProducts = {},
            onNavigateCategories = {}
        )
    }
}

