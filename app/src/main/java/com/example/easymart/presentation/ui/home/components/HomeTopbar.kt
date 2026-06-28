package com.example.easymart.presentation.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import com.example.easymart.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import androidx.compose.foundation.layout.width
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopbar(
    onCartClick: () -> Unit,
    onSearchClick: () -> Unit,
    cartCount: Int,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
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
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimens.spaceSm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
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
    Box(modifier = Modifier.padding(horizontal = dimens.spaceMd).clickable(onClick = onClick)) {
        Icon(
            painter = painterResource(R.drawable.ic_cart),
            contentDescription = "Giỏ hàng",
            tint = MaterialTheme.colorScheme.onPrimary
        )
        if (count > 0) {
            Surface(
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = (-2).dp, y = (2).dp),
                tonalElevation = 40.dp,
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
