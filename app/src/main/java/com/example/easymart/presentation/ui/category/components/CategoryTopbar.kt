package com.example.easymart.presentation.ui.category.components
import androidx.compose.ui.res.stringResource

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.R
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryTopbar(
    onCartClick: () -> Unit,
    searchQuery: String,
    onSearchTextChange: (String) -> Unit,
    onSearchClick: (String) -> Unit,
    cartCount: Int,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            CategorySearchBoxTopBar(
                text = searchQuery,
                onTextChange = onSearchTextChange,
                onSearchClick = onSearchClick
            )
        },
        actions = {
            CartIconWithBadgeItem(
                count = cartCount,
                onClick = onCartClick
            )
        }
    )
}

@Composable
fun CategorySearchBoxTopBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSearchClick: (String) -> Unit,
    placeholder: String = "Tìm kiếm sản phẩm"
) {
    val dimens = LocalAppDimens.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimens.buttonHeight),
        shape = RoundedCornerShape(dimens.radiusXl),
        color = MaterialTheme.colorScheme.surface
    ) {
        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimens.spaceSm),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { onSearchClick(text) }
            )
        ) { innerTextField ->
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null,
                    modifier = Modifier.size(dimens.iconSmall),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(dimens.spaceSm))
                Box(modifier = Modifier.weight(1f)) {
                    if (text.isBlank()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    innerTextField()
                }
                if (text.isNotBlank()) {
                    IconButton(onClick = { onTextChange("") }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(R.string.ui_text_069),
                            modifier = Modifier.size(dimens.iconSmall),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartIconWithBadgeItem(
    count: Int,
    onClick: () -> Unit
) {
    val dimens = LocalAppDimens.current
    Box(
        modifier = Modifier
            .padding(horizontal = dimens.spaceMd)
            .clickable(onClick = onClick)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_cart),
            contentDescription = stringResource(R.string.ui_text_211),
            tint = MaterialTheme.colorScheme.onPrimary
        )
        if (count > 0) {
            Surface(
                modifier = Modifier
                    .size(dimens.iconSmall)
                    .align(Alignment.TopEnd)
                    .offset(x = -dimens.spaceXs, y = dimens.spaceXs),
                tonalElevation = dimens.cardElevation,
                shape = CircleShape,
                color = MaterialTheme.colorScheme.error.copy(alpha = 0.85f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = if (count > 99) "99+" else count.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CartIconWithBadgeItemPreview() {
    EasyMartTheme {
        CartIconWithBadgeItem(
            count = 5,
            onClick = {}
        )
    }
}

@Preview
@Composable
fun CategorySearchBoxTopBarPreview() {
    EasyMartTheme {
        CategorySearchBoxTopBar(
            text = "",
            onTextChange = {},
            onSearchClick = {}
        )
    }
}

@Preview
@Composable
fun CategoryTopbarPreview() {
    EasyMartTheme {
        CategoryTopbar(
            onCartClick = {},
            searchQuery = "",
            onSearchTextChange = {},
            onSearchClick = {},
            cartCount = 5
        )
    }
}
