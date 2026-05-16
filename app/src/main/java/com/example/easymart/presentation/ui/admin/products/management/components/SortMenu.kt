package com.example.easymart.presentation.ui.admin.products.management.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.admin.products.components.sortLabel
import com.example.easymart.presentation.ui.admin.products.management.AdminProductSort


@Composable
fun SortMenu(
    sortType: AdminProductSort,
    onSelectSort: (AdminProductSort) -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        AssistChip(
            onClick = { expanded = true },
            label = {
                Text(
                    text = sortLabel(sortType),
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Sort,
                    contentDescription = null,
                    modifier = Modifier.size(dimens.iconSmall)
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(dimens.iconSmall)
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(20),
            tonalElevation = 6.dp,
            shadowElevation = 12.dp,
            modifier = Modifier.widthIn(min = 180.dp).background(MaterialTheme.colors.surface)
        ) {
            SortMenuItem(
                title = sortLabel(AdminProductSort.UPDATED_DESC),
                selected = sortType == AdminProductSort.UPDATED_DESC,
                onClick = {
                    onSelectSort(AdminProductSort.UPDATED_DESC)
                    expanded = false
                }
            )
            SortMenuItem(
                title = sortLabel(AdminProductSort.NAME_ASC),
                selected = sortType == AdminProductSort.NAME_ASC,
                onClick = {
                    onSelectSort(AdminProductSort.NAME_ASC)
                    expanded = false
                }
            )
            SortMenuItem(
                title = sortLabel(AdminProductSort.PRICE_ASC),
                selected = sortType == AdminProductSort.PRICE_ASC,
                onClick = {
                    onSelectSort(AdminProductSort.PRICE_ASC)
                    expanded = false
                }
            )
        }
    }
}

//@Composable
//private fun SortMenuItem(
//    title: String,
//    selected: Boolean,
//    onClick: () -> Unit
//) {
//        DropdownMenuItem(
//            text = { Text(title) },
//            onClick = onClick,
//            modifier = Modifier.fillMaxWidth(),
//            leadingIcon = {
//                if (selected) {
//                    Icon(
//                        imageVector = Icons.Default.Check,
//                        contentDescription = null,
//                        tint = MaterialTheme.colors.primary
//                    )
//                }
//            }
//        )
//}

@Composable
private fun SortMenuItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Text(
                text = title,
                style = MaterialTheme.typography.body2
            )
        },
        leadingIcon = {
            if (selected) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    tint = MaterialTheme.colors.primary,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        },
        onClick = onClick
    )
}

@Preview
@Composable
fun SortMenuPreview() {
    EasyMartTheme {
        SortMenu(
            sortType = AdminProductSort.UPDATED_DESC,
            onSelectSort = {}
        )
    }
}

@Preview
@Composable
fun SortMenuItemPreview() {
    EasyMartTheme {
        SortMenuItem(
            title = "Sắp xếp theo ngày cập nhật",
            selected = true,
            onClick = {}
        )
    }
}