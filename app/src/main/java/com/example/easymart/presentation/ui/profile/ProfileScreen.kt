package com.example.easymart.presentation.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.R
import com.example.easymart.domain.model.User
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.profile.components.ProfileRowItem

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    user: User? = null,
    onOptionClick: (tag: String) -> Unit = {}
) {
    val dimens = LocalAppDimens.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(dimens.spaceMd)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(dimens.space4xl))
        Card(
            modifier = Modifier.size(dimens.avatarCardSize),
            shape = CircleShape,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(defaultElevation = dimens.cardElevation),
            ) {
            Image(
                painter = painterResource(R.drawable.pic_avatar_placeholder),
                contentDescription = "User Avatar",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Name
        Text(
            text = user?.name ?: "Bạn chưa đăng nhập",
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = dimens.spaceLg),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Email
        Text(
            text = user?.email ?: "",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Email
        Text(
            text = user?.role ?: "",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth().padding(all = dimens.spaceLg),
            thickness = dimens.dividerThickness,
        )

        ProfileRowItem(
            iconRes = R.drawable.ic_orders,
            title = stringResource(R.string.label_orders),
            tag = "orders",
            onClick = onOptionClick
        )

        ProfileRowItem(
            iconRes = R.drawable.ic_address,
            title = stringResource(R.string.label_address),
            tag = "address",
            onClick = onOptionClick
        )

        ProfileRowItem(
            iconRes = R.drawable.ic_wallet,
            title = stringResource(R.string.label_payments),
            tag = "payment",
            onClick = onOptionClick
        )

        ProfileRowItem(
            iconRes = R.drawable.ic_setting,
            title = stringResource(R.string.label_settings),
            tag = "settings",
            onClick = onOptionClick
        )

        if(user != null) {
            ProfileRowItem(
                iconRes = R.drawable.ic_logout,
                title = stringResource(R.string.label_logout),
                tag = "logout",
                onClick = onOptionClick
            )
        } else {
            ProfileRowItem(
                iconRes = R.drawable.ic_login,
                title = stringResource(R.string.label_login),
                tag = "login",
                onClick = onOptionClick
            )
        }


    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    EasyMartTheme {
        ProfileScreen(
            user = User(
                id = "1",
                name = "John Doe",
                email = "johnydangx2qn@gmail.com",
                phone = "1234567890",
                createdAt = System.currentTimeMillis()
            )
        )
    }
}