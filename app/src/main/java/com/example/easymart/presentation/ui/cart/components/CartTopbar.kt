package com.example.easymart.presentation.ui.cart.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.easymart.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartTopbar(
    selectedCount: Int,
    onBackClick: () -> Unit,
    onDeleteSelectedClick: () -> Unit
) {
    val canDelete = selectedCount > 0
    TopAppBar(
        title = { Text(text = "Giỏ hàng") },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(
                onClick = onDeleteSelectedClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Xóa sản phẩm đã chọn",
                    tint = if (canDelete) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    )
}
