package com.example.easymart.presentation.ui.category.components
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun CategorySortFilterBar(
    selectedSort: CustomerProductSort,
    selectedStockFilter: CustomerProductStockFilter,
    selectedPriceFilter: CustomerProductPriceFilter,
    selectedRatingFilter: CustomerProductRatingFilter,
    onSortSelected: (CustomerProductSort) -> Unit,
    onStockFilterSelected: (CustomerProductStockFilter) -> Unit,
    onPriceFilterSelected: (CustomerProductPriceFilter) -> Unit,
    onRatingFilterSelected: (CustomerProductRatingFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterDropdownPill(
            label = stringResource(R.string.ui_text_220),
            value = selectedSort.label,
            icon = sortIcon(selectedSort),
            selectedValue = selectedSort,
            options = sortOptions(),
            onSelected = onSortSelected
        )

        FilterDropdownPill(
            label = stringResource(R.string.ui_text_221),
            value = selectedPriceFilter.label,
            icon = Icons.Filled.LocalOffer,
            selectedValue = selectedPriceFilter,
            options = priceOptions(),
            onSelected = onPriceFilterSelected
        )

        FilterDropdownPill(
            label = stringResource(R.string.ui_text_222),
            value = selectedStockFilter.label,
            icon = Icons.Filled.Inventory2,
            selectedValue = selectedStockFilter,
            options = stockOptions(),
            onSelected = onStockFilterSelected
        )

        FilterDropdownPill(
            label = stringResource(R.string.ui_text_156),
            value = selectedRatingFilter.label,
            icon = Icons.Filled.Star,
            selectedValue = selectedRatingFilter,
            options = ratingOptions(),
            onSelected = onRatingFilterSelected
        )
    }
}

@Composable
private fun <T> FilterDropdownPill(
    label: String,
    value: String,
    icon: ImageVector,
    selectedValue: T,
    options: List<FilterMenuOption<T>>,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Surface(
            onClick = { expanded = true },
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.75f)
            ),
            tonalElevation = 2.dp,
            shadowElevation = 3.dp
        ) {
            Row(
                modifier = Modifier.padding(
                    horizontal = dimens.spaceMd,
                    vertical = dimens.spaceSm
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconBadge(icon = icon)
                Spacer(modifier = Modifier.width(dimens.spaceSm))
                Column {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(dimens.spaceSm))
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 8.dp,
            shadowElevation = 14.dp,
            modifier = Modifier
                .widthIn(min = 286.dp, max = 340.dp)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            options.forEach { option ->
                FilterDropdownMenuItem(
                    title = option.title,
                    description = option.description,
                    icon = option.icon,
                    selected = option.value == selectedValue,
                    onClick = {
                        expanded = false
                        onSelected(option.value)
                    }
                )
            }
        }
    }
}

@Composable
private fun FilterDropdownMenuItem(
    title: String,
    description: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        leadingIcon = {
            IconBadge(
                icon = icon,
                selected = selected
            )
        },
        trailingIcon = {
            if (selected) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
    )
}

@Composable
private fun IconBadge(
    icon: ImageVector,
    selected: Boolean = true
) {
    val backgroundColor = if (selected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)
    }
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = contentColor
        )
    }
}

private data class FilterMenuOption<T>(
    val value: T,
    val title: String,
    val description: String,
    val icon: ImageVector
)

private fun sortOptions() = listOf(
    FilterMenuOption(
        value = CustomerProductSort.FEATURED,
        title = CustomerProductSort.FEATURED.label,
        description = "Ưu tiên điểm đánh giá và lượt bán",
        icon = Icons.AutoMirrored.Filled.TrendingUp
    ),
    FilterMenuOption(
        value = CustomerProductSort.NAME_ASC,
        title = CustomerProductSort.NAME_ASC.label,
        description = "Dễ quét theo tên sản phẩm",
        icon = Icons.Filled.SortByAlpha
    ),
    FilterMenuOption(
        value = CustomerProductSort.PRICE_ASC,
        title = CustomerProductSort.PRICE_ASC.label,
        description = "Từ lựa chọn tiết kiệm đến cao cấp",
        icon = Icons.Filled.ArrowUpward
    ),
    FilterMenuOption(
        value = CustomerProductSort.PRICE_DESC,
        title = CustomerProductSort.PRICE_DESC.label,
        description = "Ưu tiên sản phẩm giá cao",
        icon = Icons.Filled.ArrowDownward
    ),
    FilterMenuOption(
        value = CustomerProductSort.SOLD_DESC,
        title = CustomerProductSort.SOLD_DESC.label,
        description = "Sản phẩm đang được mua nhiều",
        icon = Icons.Filled.Sell
    )
)

private fun priceOptions() = CustomerProductPriceFilter.entries.map { filter ->
    FilterMenuOption(
        value = filter,
        title = filter.label,
        description = filter.description,
        icon = Icons.Filled.LocalOffer
    )
}

private fun stockOptions() = CustomerProductStockFilter.entries.map { filter ->
    FilterMenuOption(
        value = filter,
        title = filter.label,
        description = when (filter) {
            CustomerProductStockFilter.ALL -> "Bao gồm mọi trạng thái kho"
            CustomerProductStockFilter.IN_STOCK -> "Chỉ sản phẩm có thể thêm vào giỏ"
            CustomerProductStockFilter.OUT_OF_STOCK -> "Sản phẩm đang tạm hết"
        },
        icon = Icons.Filled.Inventory2
    )
}

private fun ratingOptions() = CustomerProductRatingFilter.entries.map { filter ->
    FilterMenuOption(
        value = filter,
        title = filter.label,
        description = filter.description,
        icon = Icons.Filled.Star
    )
}

private fun sortIcon(sort: CustomerProductSort): ImageVector {
    return when (sort) {
        CustomerProductSort.FEATURED -> Icons.AutoMirrored.Filled.TrendingUp
        CustomerProductSort.NAME_ASC -> Icons.Filled.SortByAlpha
        CustomerProductSort.PRICE_ASC -> Icons.Filled.ArrowUpward
        CustomerProductSort.PRICE_DESC -> Icons.Filled.ArrowDownward
        CustomerProductSort.SOLD_DESC -> Icons.Filled.Sell
    }
}

@Preview(showBackground = true)
@Composable
fun CategorySortFilterBarPreview() {
    EasyMartTheme {
        CategorySortFilterBar(
            selectedSort = CustomerProductSort.FEATURED,
            selectedStockFilter = CustomerProductStockFilter.IN_STOCK,
            selectedPriceFilter = CustomerProductPriceFilter.FROM_500K_TO_1M,
            selectedRatingFilter = CustomerProductRatingFilter.FOUR_PLUS,
            onSortSelected = {},
            onStockFilterSelected = {},
            onPriceFilterSelected = {},
            onRatingFilterSelected = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
