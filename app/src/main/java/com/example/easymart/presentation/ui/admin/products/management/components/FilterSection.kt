package com.example.easymart.presentation.ui.admin.products.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun FilterSection(
    categories: List<String>,
    selectedCategory: String?,
    onlyLowStock: Boolean,
    onSelectCategory: (String?) -> Unit,
    onToggleLowStock: (Boolean) -> Unit
) {
    val dimens = LocalAppDimens.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.screenPadding)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.FilterAlt,
                contentDescription = null,
                modifier = Modifier.size(dimens.iconSmall)
            )
            Spacer(modifier = Modifier.width(dimens.spaceXs))
            Text(text = "Bộ lọc", style = MaterialTheme.typography.titleSmall)
        }

        Spacer(modifier = Modifier.height(dimens.spaceSm))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm)
        ) {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onSelectCategory(null) },
                label = { Text("Tất cả") }
            )
            FilterChip(
                selected = onlyLowStock,
                onClick = { onToggleLowStock(!onlyLowStock) },
                label = { Text("Sắp hết") }
            )
        }

        if (categories.isNotEmpty()) {
            Spacer(modifier = Modifier.height(dimens.spaceSm))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm)
            ) {
                categories.forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { onSelectCategory(category) },
                        label = { Text(category) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun FilterSectionPreview() {
    val categories = listOf("Điện tử", "Thời trang", "Gia dụng", "Sách", "Thể thao")
    EasyMartTheme {
        FilterSection(
            categories = categories,
            selectedCategory = "Điện tử",
            onlyLowStock = true,
            onSelectCategory = {},
            onToggleLowStock = {}
        )
    }
}