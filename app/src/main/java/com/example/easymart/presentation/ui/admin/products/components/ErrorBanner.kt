package com.example.easymart.presentation.ui.admin.products.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun ErrorBanner(message: String, onRefresh: () -> Unit) {
    val dimens = LocalAppDimens.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimens.screenPadding),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.spaceMd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(dimens.spaceSm))
            FilledTonalButton(onClick = onRefresh) {
                Text("Thử lại")
            }
        }
    }
}

@Preview
@Composable
fun ErrorBannerPreview() {
    EasyMartTheme {
        ErrorBanner(message = "Lỗi kết nối", onRefresh = {})
    }
}