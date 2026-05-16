package com.example.easymart.presentation.ui.admin.products.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun HeaderSection(
    query: String,
    onQueryChange: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onAddProduct: () -> Unit
) {
    val dimens = LocalAppDimens.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.screenPadding, vertical = dimens.spaceSm)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                IconButton(onClick = onNavigateBack) {
//                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
//                }
//                Text(
//                    text = "Quản lý sản phẩm",
//                    style = MaterialTheme.typography.titleLarge,
//                    color = MaterialTheme.colorScheme.onBackground
//                )
//            }


            Button(
                onClick = onAddProduct,
                shape = RoundedCornerShape(dimens.radiusXl),
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            ) {
                Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(dimens.iconMedium))
                Spacer(modifier = Modifier.width(dimens.spaceXs))
                Text("Thêm sản phẩm")
            }
        }

        Spacer(modifier = Modifier.height(dimens.spaceSm))
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Tìm theo tên sản phẩm, danh mục...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Tune,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
            onNavigateBack = {},
            onAddProduct = {}
        )
    }
}