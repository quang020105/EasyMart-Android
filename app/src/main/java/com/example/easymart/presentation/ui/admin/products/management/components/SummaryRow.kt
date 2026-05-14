package com.example.easymart.presentation.ui.admin.products.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun SummaryRow(total: Int, lowStockCount: Int) {
    val dimens = LocalAppDimens.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimens.spaceMd)
    ) {
        SummaryChip(label = "Tổng", value = total.toString())
        SummaryChip(label = "Sắp hết", value = lowStockCount.toString())
    }
}

@Composable
private fun SummaryChip(label: String, value: String) {
    ElevatedAssistChip(
        onClick = {},
        label = { Text("$label: $value") }
    )
}

@Preview
@Composable
fun SummaryRowPreview() {
    EasyMartTheme {
        SummaryRow(total = 120, lowStockCount = 5)
    }
}