package com.example.easymart.presentation.ui.home.components
import androidx.compose.ui.res.stringResource

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.R
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.colors.LocalAppColors
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopbar(
    onCartClick: () -> Unit,
    onSearchClick: () -> Unit,
    cartCount: Int,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = {
            SearchBoxTopBar(onClick = onSearchClick)
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
fun SearchBoxTopBar(
    onClick: () -> Unit,
    placeholder: String = "Tìm kiếm mọi thứ"
) {
    val dimens = LocalAppDimens.current
    val appColors = LocalAppColors.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimens.searchInputHeight)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(dimens.radiusXl),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = dimens.dividerThickness
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimens.spaceMd),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier.size(dimens.iconSmall),
                tint = appColors.iconMuted
            )
            Spacer(modifier = Modifier.width(dimens.spaceSm))
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
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
            .size(dimens.buttonHeight)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_cart),
            contentDescription = stringResource(R.string.ui_text_211),
            modifier = Modifier.size(dimens.iconLarge),
            tint = MaterialTheme.colorScheme.onPrimary
        )
        if (count > 0) {
            Surface(
                modifier = Modifier
                    .size(dimens.qtyBtnSize)
                    .align(Alignment.TopEnd),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
                shadowElevation = dimens.dividerThickness
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = if (count > 99) "99+" else count.toString(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 1
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
fun SearchBoxTopBarPreview() {
    EasyMartTheme {
        SearchBoxTopBar(
            onClick = {}
        )
    }
}

@Preview
@Composable
fun HomeTopbarPreview() {
    EasyMartTheme {
        HomeTopbar(
            onCartClick = {},
            onSearchClick = {},
            cartCount = 5
        )
    }
}
