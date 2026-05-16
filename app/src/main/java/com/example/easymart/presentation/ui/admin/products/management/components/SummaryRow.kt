package com.example.easymart.presentation.ui.admin.products.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun SummaryRow(total: Int, visibleCount: Int) {
    val dimens = LocalAppDimens.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.radiusLarge),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.spaceMd),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SummaryItem(label = "Tổng sản phẩm", value = total.toString())
            SummaryItem(label = "Đã hiển thị", value = visibleCount.toString(), highlight = true)
        }
    }
}

@Composable
private fun SummaryItem(label: String, value: String, highlight: Boolean = false) {
    Column {
        Text(text = label, style = MaterialTheme.typography.bodySmall)
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = if (highlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
fun SummaryRowPreview() {
    EasyMartTheme {
        SummaryRow(total = 26, visibleCount = 20)
    }
}