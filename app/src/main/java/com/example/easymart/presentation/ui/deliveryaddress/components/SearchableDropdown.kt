package com.example.easymart.presentation.ui.deliveryaddress.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.util.TableInfo
import com.example.easymart.R
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.deliveryaddress.AddAddressScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SearchableDropDown(
    modifier: Modifier = Modifier,
    label: String,
    options: List<T>,
    selected: T?,
    onSelect: (T) -> Unit,
    itemLabel: (T) -> String,
    enabled: Boolean = true,
    //điều khiển hành vi focus / tương tác khi menu mở
    anchorType: ExposedDropdownMenuAnchorType = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
) {
    val dimens = LocalAppDimens.current
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (enabled) expanded = !expanded
        },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selected?.let { itemLabel(it) } ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(text = label) },
            enabled = enabled,
            singleLine = true,
            trailingIcon = {

                    Icon(
                        painter = painterResource(R.drawable.ic_chevron_right),
                        contentDescription = null,
                        modifier = Modifier.size(dimens.iconSize)
                    )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = enabled) { expanded = true }
                .menuAnchor(anchorType, enabled)
        )

        //danh cách các option
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = dimens.spaceMd)
        ) {
            if (options.isEmpty()) {
                DropdownMenuItem(
                    text = { Text(text = "Không có kết quả") },
                    onClick = {}
                )
            } else {
                options.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = itemLabel(item)) },
                        onClick = {
                            onSelect(item)
                            expanded = false
                        },
                        modifier = Modifier.background(MaterialTheme.colorScheme.background)
                    )
                }

            }
        }
    }

}

@Preview
@Composable
fun SearchableDropDownPreview(

) {
    EasyMartTheme {
        // Mock data
        val sampleOptions = listOf("Hà Nội", "Đà Nẵng", "Hồ Chí Minh")

        var selected by remember { mutableStateOf<String?>(null) }

        SearchableDropDown(
            label = "Tỉnh/Thành phố",
            options = sampleOptions,
            selected = selected,
            onSelect = { selected = it },
            itemLabel = { it }
        )
    }
}