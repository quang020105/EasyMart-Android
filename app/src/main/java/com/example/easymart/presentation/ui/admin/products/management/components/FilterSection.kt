package com.example.easymart.presentation.ui.admin.products.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.ui.admin.products.management.AdminProductSort
import com.example.easymart.presentation.ui.admin.products.management.ProductSourceFilter

@Composable
fun FilterSection(
    totalCount: Int,
    apiCount: Int,
    addedCount: Int,
    hiddenCount: Int,
    outOfStockCount: Int,
    categories: List<String>,
    selectedCategory: String?,
    onlyLowStock: Boolean,
    sourceFilter: ProductSourceFilter,
    sortType: AdminProductSort,
    onSelectCategory: (String?) -> Unit,
    onToggleLowStock: (Boolean) -> Unit,
    onSelectSource: (ProductSourceFilter) -> Unit,
    onSelectSort: (AdminProductSort) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProductFilterChip(
                text = "Tất cả ($totalCount)",
                selected = sourceFilter == ProductSourceFilter.ALL,
                onClick = { onSelectSource(ProductSourceFilter.ALL) }
            )

            ProductFilterChip(
                text = "Có sẵn từ API ($apiCount)",
                selected = sourceFilter == ProductSourceFilter.API,
                onClick = { onSelectSource(ProductSourceFilter.API) }
            )

            ProductFilterChip(
                text = "Đã thêm ($addedCount)",
                selected = sourceFilter == ProductSourceFilter.ADDED,
                onClick = { onSelectSource(ProductSourceFilter.ADDED) }
            )

            ProductFilterChip(
                text = "Đã ẩn ($hiddenCount)",
                selected = false,
                onClick = {}
            )

            ProductFilterChip(
                text = "Hết hàng ($outOfStockCount)",
                selected = onlyLowStock,
                onClick = { onToggleLowStock(!onlyLowStock) }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryFilterChip(
                text = "Tất cả",
                icon = Icons.Filled.Tune,
                selected = selectedCategory == null,
                onClick = { onSelectCategory(null) }
            )

            categories.forEach { category ->
                CategoryFilterChip(
                    text = category,
                    icon = categoryIcon(category),
                    selected = selectedCategory == category,
                    onClick = { onSelectCategory(category) }
                )
            }
        }
    }
}

@Composable
private fun ProductFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = if (selected) Color(0xFFEAF2FF) else MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) Color(0xFFBFDBFE)
            else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)
        ),
        shadowElevation = if (selected) 3.dp else 2.dp,
        tonalElevation = if (selected) 1.dp else 0.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                color = if (selected) Color(0xFF2563EB)
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Composable
private fun CategoryFilterChip(
    text: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = if (selected) Color(0xFFEAF2FF) else MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) Color(0xFFBFDBFE)
            else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)
        ),
        shadowElevation = if (selected) 3.dp else 2.dp,
        tonalElevation = if (selected) 1.dp else 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = if (selected) Color(0xFF2563EB)
                else MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                    color = if (selected) Color(0xFF2563EB)
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

private fun categoryIcon(category: String): ImageVector {
    return when {
        category.contains("điện", ignoreCase = true) -> Icons.Filled.Devices
        category.contains("laptop", ignoreCase = true) -> Icons.Filled.Devices
        category.contains("thời", ignoreCase = true) -> Icons.Filled.ShoppingBag
        category.contains("phụ", ignoreCase = true) -> Icons.Filled.Inventory2
        else -> Icons.Filled.Category
    }
}

fun sortLabel(sortType: AdminProductSort): String {
    return when (sortType) {
        AdminProductSort.UPDATED_DESC -> "Mới cập nhật"
        AdminProductSort.NAME_ASC -> "Tên A-Z"
        AdminProductSort.PRICE_ASC -> "Giá thấp"
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterSectionPreview() {
    EasyMartTheme {
        FilterSection(
            totalCount = 27,
            apiCount = 20,
            addedCount = 7,
            hiddenCount = 4,
            outOfStockCount = 3,
            categories = listOf("Điện thoại", "Laptop", "Phụ kiện", "Thời trang"),
            selectedCategory = null,
            onlyLowStock = false,
            sourceFilter = ProductSourceFilter.ALL,
            sortType = AdminProductSort.UPDATED_DESC,
            onSelectCategory = {},
            onToggleLowStock = {},
            onSelectSource = {},
            onSelectSort = {}
        )
    }
}