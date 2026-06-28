package com.example.easymart.presentation.ui.category.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun CategorySortFilterBar(
    selectedSort: CustomerProductSort,
    selectedStockFilter: CustomerProductStockFilter,
    onSortSelected: (CustomerProductSort) -> Unit,
    onStockFilterSelected: (CustomerProductStockFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    var sortMenuExpanded by remember { mutableStateOf(false) }
    var stockMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm)
    ) {
        FilterDropdownPill(
            text = selectedSort.label,
            expanded = sortMenuExpanded,
            onClick = { sortMenuExpanded = true },
            onDismiss = { sortMenuExpanded = false }
        ) {
            CustomerProductSort.entries.forEach { sort ->
                DropdownMenuItem(
                    text = { Text(text = sort.label) },
                    onClick = {
                        sortMenuExpanded = false
                        onSortSelected(sort)
                    }
                )
            }
        }

        FilterDropdownPill(
            text = selectedStockFilter.label,
            expanded = stockMenuExpanded,
            onClick = { stockMenuExpanded = true },
            onDismiss = { stockMenuExpanded = false }
        ) {
            CustomerProductStockFilter.entries.forEach { filter ->
                DropdownMenuItem(
                    text = { Text(text = filter.label) },
                    onClick = {
                        stockMenuExpanded = false
                        onStockFilterSelected(filter)
                    }
                )
            }
        }
    }
}

@Composable
private fun FilterDropdownPill(
    text: String,
    expanded: Boolean,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    menuContent: @Composable () -> Unit
) {
    val dimens = LocalAppDimens.current

    Box(modifier = modifier) {
        Surface(
            onClick = onClick,
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
            contentColor = MaterialTheme.colorScheme.primary,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
            ),
            shadowElevation = 0.dp
        ) {
            Text(
                text = "$text ▾",
                modifier = Modifier.padding(
                    horizontal = dimens.spaceLg,
                    vertical = dimens.spaceSm
                ),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss
        ) {
            menuContent()
        }
    }
}

@Preview
@Composable
fun CategorySortFilterBarPreview() {
    val dimens = LocalAppDimens.current
    CategorySortFilterBar(
        selectedSort = CustomerProductSort.FEATURED,
        selectedStockFilter = CustomerProductStockFilter.IN_STOCK,
        onSortSelected = {},
        onStockFilterSelected = {},
        modifier = Modifier.padding(dimens.spaceMd)
    )
}