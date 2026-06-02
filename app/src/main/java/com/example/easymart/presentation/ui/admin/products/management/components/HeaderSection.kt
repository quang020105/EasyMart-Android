package com.example.easymart.presentation.ui.admin.products.management.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun HeaderSection(
    query: String,
    onQueryChange: (String) -> Unit,
    isFilterVisible: Boolean,
    hasActiveFilter: Boolean,
    onToggleFilterClick: () -> Unit
) {
    val dimens = LocalAppDimens.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.screenPadding, vertical = dimens.spaceSm)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    "Tìm theo tên, danh mục",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            trailingIcon = {
                IconButton(
                    onClick = onToggleFilterClick
                ) {
                    BadgedBox(
                        badge = {
                            if (hasActiveFilter && !isFilterVisible) {
                                Badge()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isFilterVisible) {
                                Icons.Filled.Close
                            } else {
                                Icons.Outlined.Tune
                            },
                            contentDescription = if (isFilterVisible) {
                                "Ẩn bộ lọc"
                            } else {
                                "Hiện bộ lọc"
                            },
                            tint = if (hasActiveFilter) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(dimens.radiusLarge)
        )

    }
}

@Preview
@Composable
fun HeaderSectionPreview() {
    EasyMartTheme {
        HeaderSection(
            query = "",
            onQueryChange = {},
            isFilterVisible = false,
            hasActiveFilter = true,
            onToggleFilterClick = {}
        )
    }
}