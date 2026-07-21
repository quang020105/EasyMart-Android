package com.example.easymart.presentation.ui.admin.dashboard.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.easymart.domain.model.DashboardPeriod
import com.example.easymart.presentation.ui.common.components.SelectionMenuItem

@Composable
fun DashboardPeriodMenu(
    expanded: Boolean,
    selectedPeriod: DashboardPeriod,
    onDismissRequest: () -> Unit,
    onPeriodSelected: (DashboardPeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier.width(280.dp),
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 4.dp,
        shadowElevation = 10.dp,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(6.dp)) {
            Text(
                text = "Khoảng thời gian",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))

            DashboardPeriod.entries.forEach { period ->
                SelectionMenuItem(
                    title = period.label,
                    description = periodDescription(period),
                    icon = Icons.Filled.DateRange,
                    selected = period == selectedPeriod,
                    onClick = { onPeriodSelected(period) }
                )
            }
        }
    }
}

private fun periodDescription(period: DashboardPeriod): String = when (period) {
    DashboardPeriod.TODAY -> "Theo dõi hoạt động trong ngày"
    DashboardPeriod.LAST_7_DAYS -> "So sánh với 7 ngày liền trước"
    DashboardPeriod.LAST_30_DAYS -> "Theo dõi xu hướng trong tháng"
}
