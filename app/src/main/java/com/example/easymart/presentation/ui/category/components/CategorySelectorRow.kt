package com.example.easymart.presentation.ui.category.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun CategorySelectorRow(
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current

    val displayCategories = categories
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .distinctBy { it.lowercase() }

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(dimens.spaceSm),
        contentPadding = PaddingValues(horizontal = dimens.spaceXs)
    ) {
        item {
            CategoryFilterPill(
                text = "Tất cả",
                selected = selectedCategory == null,
                onClick = { onCategorySelected(null) }
            )
        }

        items(displayCategories, key = { it }) { category ->
            CategoryFilterPill(
                text = category,
                selected = selectedCategory == category,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
private fun CategoryFilterPill(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        color = if (selected) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        } else {
            MaterialTheme.colorScheme.surface
        },
        contentColor = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
        ),
        shadowElevation = if (selected) 2.dp else 0.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = dimens.spaceLg,
                vertical = dimens.spaceSm
            ),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
        )
    }
}

@Preview
@Composable
fun CategorySelectorRowPreview() {
    val categories = listOf("Điện thoại", "Laptop", "Phụ kiện", "Tablet", "Đồng hồ")
    val selectedCategory = "Laptop"

    CategorySelectorRow(
        categories = categories,
        selectedCategory = selectedCategory,
        onCategorySelected = {}
    )
}