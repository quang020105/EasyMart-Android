package com.example.easymart.presentation.ui.category.components
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.presentation.theme.EasyMartTheme
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
                text = stringResource(R.string.ui_text_179),
                icon = Icons.Filled.Tune,
                selected = selectedCategory == null,
                onClick = { onCategorySelected(null) }
            )
        }

        items(displayCategories, key = { it }) { category ->
            CategoryFilterPill(
                text = category,
                icon = categoryIcon(category),
                selected = selectedCategory == category,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
private fun CategoryFilterPill(
    text: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = if (selected) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
        } else {
            MaterialTheme.colorScheme.surface
        },
        contentColor = contentColor,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.34f)
            } else {
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.80f)
            }
        ),
        tonalElevation = if (selected) 2.dp else 0.dp,
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = dimens.spaceMd,
                vertical = dimens.spaceSm
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(contentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = contentColor
                )
            }
            Spacer(modifier = Modifier.width(dimens.spaceXs))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
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

@Preview(showBackground = true)
@Composable
fun CategorySelectorRowPreview() {
    EasyMartTheme {
        CategorySelectorRow(
            categories = listOf(stringResource(R.string.ui_text_163), stringResource(R.string.ui_text_164), stringResource(R.string.ui_text_165), stringResource(R.string.ui_text_218), stringResource(R.string.ui_text_219)),
            selectedCategory = "Laptop",
            onCategorySelected = {}
        )
    }
}
