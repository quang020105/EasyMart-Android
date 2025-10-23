package com.example.easymart.presentation.ui.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.R
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun ProfileRowItem(
    iconRes: Int,
    title: String,
    tag: String,
    onClick: (tag: String) -> Unit
) {
    val dimens = LocalAppDimens.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(tag) }
            .padding(all = dimens.spaceSm).background(color = MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(start = dimens.spaceSm).size(dimens.iconLarge)
        )

        Spacer(modifier = Modifier.width(dimens.spaceMd))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )

        // chevron or trailing action (optional)
        IconButton(onClick = { onClick(tag) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_right),
                contentDescription = "Go",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.size(dimens.iconMedium)
            )
        }
    }

    // row divider
//    Divider(
//        modifier = Modifier.padding(start = dimens.spaceSm),
//        color = MaterialTheme.colorScheme.outline
//    )
}

@Preview
@Composable
fun ProfileRowItemPreview() {
    EasyMartTheme {
        ProfileRowItem(
            iconRes = R.drawable.ic_logout,
            title = "Đăng xuất",
            tag = "logout",
            onClick = {}
        )
    }
}
