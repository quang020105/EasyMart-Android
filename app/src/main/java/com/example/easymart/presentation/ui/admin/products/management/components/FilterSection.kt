package com.example.easymart.presentation.ui.admin.products.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.admin.products.management.AdminProductSort
import com.example.easymart.presentation.ui.admin.products.management.ProductSourceFilter
import com.example.easymart.presentation.ui.admin.products.management.components.SortMenu

@Composable
fun FilterSection(
    totalCount: Int,
    apiCount: Int,
    addedCount: Int,
    categories: List<String>,
    selectedCategory: String?,
    onlyLowStock: Boolean,
    sourceFilter: ProductSourceFilter,
    sortType: AdminProductSort,
    onSelectCategory: (String?) -> Unit,
    onToggleLowStock: (Boolean) -> Unit,
    onSelectSource: (ProductSourceFilter) -> Unit,
    onSelectSort: (AdminProductSort) -> Unit
) {
    val dimens = LocalAppDimens.current
    var sortExpanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.screenPadding)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                selected = sourceFilter == ProductSourceFilter.ALL,
                onClick = { onSelectSource(ProductSourceFilter.ALL) },
                label = { Text("Tất cả ($totalCount)") }
            )
            FilterChip(
                selected = sourceFilter == ProductSourceFilter.API,
                onClick = { onSelectSource(ProductSourceFilter.API) },
                label = { Text("Có sẵn từ API ($apiCount)") }
            )
            FilterChip(
                selected = sourceFilter == ProductSourceFilter.ADDED,
                onClick = { onSelectSource(ProductSourceFilter.ADDED) },
                label = { Text("Đã thêm ($addedCount)") }
            )
//            Box {
//                Card(
//                    onClick = { sortExpanded = true },
//                    shape = RoundedCornerShape(dimens.radiusLarge),
//                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
//                ) {
//                    Row(
//                        modifier = Modifier.padding(horizontal = dimens.spaceSm, vertical = dimens.spaceXs),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(sortLabel(sortType), style = MaterialTheme.typography.labelLarge)
//                        Spacer(modifier = Modifier.width(dimens.spaceXs))
//                        Icon(
//                            Icons.AutoMirrored.Filled.Sort,
//                            contentDescription = null,
//                            modifier = Modifier.size(dimens.iconSmall)
//                        )
//                    }
//                }
//
//                DropdownMenu(
//                    expanded = sortExpanded,
//                    onDismissRequest = { sortExpanded = false }
//                ) {
//                    DropdownMenuItem(
//                        text = { Text(sortLabel(AdminProductSort.UPDATED_DESC)) },
//                        onClick = {
//                            onSelectSort(AdminProductSort.UPDATED_DESC)
//                            sortExpanded = false
//                        }
//                    )
//                    DropdownMenuItem(
//                        text = { Text(sortLabel(AdminProductSort.NAME_ASC)) },
//                        onClick = {
//                            onSelectSort(AdminProductSort.NAME_ASC)
//                            sortExpanded = false
//                        }
//                    )
//                    DropdownMenuItem(
//                        text = { Text(sortLabel(AdminProductSort.PRICE_ASC)) },
//                        onClick = {
//                            onSelectSort(AdminProductSort.PRICE_ASC)
//                            sortExpanded = false
//                        }
//                    )
//                }
//            }

            SortMenu(
                sortType = sortType,
                onSelectSort = onSelectSort
            )
        }

        Spacer(modifier = Modifier.height(dimens.spaceSm))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm)
        ) {
            Surface(
                shape = RoundedCornerShape(dimens.radiusLarge),
                color = if (selectedCategory == null) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                onClick = { onSelectCategory(null) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = dimens.spaceSm, vertical = dimens.spaceXs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Category, contentDescription = null, modifier = Modifier.size(dimens.iconSmall))
                    Spacer(modifier = Modifier.width(dimens.spaceXs))
                    Text("Tất cả")
                }
            }
            categories.forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { onSelectCategory(category) },
                    label = { Text(category) }
                )
            }
            FilterChip(
                selected = onlyLowStock,
                onClick = { onToggleLowStock(!onlyLowStock) },
                label = { Text("Sắp hết") }
            )
        }
    }
}

fun sortLabel(sortType: AdminProductSort): String {
    return when (sortType) {
        AdminProductSort.UPDATED_DESC -> "Mới cập nhật"
        AdminProductSort.NAME_ASC -> "Tên A-Z"
        AdminProductSort.PRICE_ASC -> "Giá thấp"
    }
}

@Preview
@Composable
fun FilterSectionPreview() {
    val categories = listOf("Điện tử", "Thời trang", "Gia dụng", "Sách", "Thể thao")
    EasyMartTheme {
        FilterSection(
            totalCount = 26,
            apiCount = 20,
            addedCount = 6,
            categories = categories,
            selectedCategory = "Điện tử",
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