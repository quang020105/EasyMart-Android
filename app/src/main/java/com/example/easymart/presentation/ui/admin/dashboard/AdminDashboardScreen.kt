package com.example.easymart.presentation.ui.admin.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.easymart.R
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.admin.dashboard.AdminDashboardUiState
import kotlin.math.roundToInt

@Composable
fun AdminDashboardScreen(
    uiState: AdminDashboardUiState,
    onRefresh: () -> Unit,
    onNavigateOrders: () -> Unit,
    onNavigateProducts: () -> Unit,
    onNavigateCategories: () -> Unit
) {
    val dimens = LocalAppDimens.current

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimens.screenPadding)
        ) {
            Text(
                text = "Dashboard",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Tổng quan hoạt động hôm nay",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(dimens.spaceMd))

            SummaryRow(
                revenueToday = uiState.revenueToday,
                orderCount = uiState.orderCount
            )

            Spacer(modifier = Modifier.height(dimens.spaceMd))

            QuickActionsSection(
                onNavigateOrders = onNavigateOrders,
                onNavigateProducts = onNavigateProducts,
                onNavigateCategories = onNavigateCategories
            )

            Spacer(modifier = Modifier.height(dimens.spaceMd))

            Text(
                text = "Thống kê nhanh",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(dimens.spaceSm))

            StatsCard(
                title = "Sản phẩm",
                value = uiState.productCount,
                iconRes = R.drawable.ic_category
            )
            Spacer(modifier = Modifier.height(dimens.spaceSm))
            StatsCard(
                title = "Danh mục",
                value = uiState.categoryCount,
                iconRes = R.drawable.ic_category
            )
            Spacer(modifier = Modifier.height(dimens.spaceSm))
            StatsCard(
                title = "Đơn hàng",
                value = uiState.orderCount,
                iconRes = R.drawable.ic_orders
            )

            Spacer(modifier = Modifier.height(dimens.spaceLg))

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

@Composable
private fun SummaryRow(revenueToday: Double, orderCount: Int) {
    val dimens = LocalAppDimens.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimens.spaceMd)
    ) {
        SummaryCard(
            title = "Doanh thu",
            value = formatCurrency(revenueToday),
            subtitle = "Hôm nay",
            modifier = Modifier.weight(1f)
        )
        SummaryCard(
            title = "Đơn hàng",
            value = orderCount.toString(),
            subtitle = "Hôm nay",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SummaryCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(dimens.spaceMd)) {
            Text(text = title, style = MaterialTheme.typography.labelLarge)
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun QuickActionsSection(
    onNavigateOrders: () -> Unit,
    onNavigateProducts: () -> Unit,
    onNavigateCategories: () -> Unit
) {
    val dimens = LocalAppDimens.current
    Text(
        text = "Chức năng nhanh",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier.height(dimens.spaceSm))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimens.spaceMd)
    ) {
        QuickActionButton(
            text = "Đơn hàng",
            iconRes = R.drawable.ic_orders,
            onClick = onNavigateOrders,
            modifier = Modifier.weight(1f)
        )
        QuickActionButton(
            text = "Sản phẩm",
            iconRes = R.drawable.ic_category,
            onClick = onNavigateProducts,
            modifier = Modifier.weight(1f)
        )
        QuickActionButton(
            text = "Danh mục",
            iconRes = R.drawable.ic_category,
            onClick = onNavigateCategories,
            modifier = Modifier.weight(1f)
        )
    }

    HorizontalDivider(modifier = Modifier.padding(top = dimens.spaceMd))
}

@Composable
private fun QuickActionButton(
    text: String,
    iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier.height(dimens.buttonHeight),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(dimens.iconSmall)
        )
        Spacer(modifier = Modifier.size(dimens.spaceXs))
        Text(text = text)
    }
}

@Composable
private fun StatsCard(title: String, value: Int, iconRes: Int) {
    val dimens = LocalAppDimens.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.spaceMd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(dimens.iconMedium)
                        .clip(RoundedCornerShape(dimens.radiusSmall))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(dimens.spaceSm)
                )
                Spacer(modifier = Modifier.size(dimens.spaceSm))
                Text(text = title, style = MaterialTheme.typography.bodyLarge)
            }
            Text(text = value.toString(), style = MaterialTheme.typography.titleMedium)
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

private fun formatCurrency(value: Double): String {
    val formatted = (value / 1000.0).roundToInt() * 1000
    return "${"%,d".format(formatted.toLong())} đ"
}

