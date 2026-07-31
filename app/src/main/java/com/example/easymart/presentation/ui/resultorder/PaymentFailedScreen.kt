package com.example.easymart.presentation.ui.resultorder
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun PaymentFailedScreen(
    message: String,
    onRetry: () -> Unit,
    onChangePaymentMethod: () -> Unit,
    onBackToCheckOut: () -> Unit
) {
    val dimens = LocalAppDimens.current
    Box (
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
    ){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimens.spaceXl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(dimens.space4xl)
            )

            Spacer(modifier = Modifier.height(dimens.spaceLg))

            Text(
                text = stringResource(R.string.ui_text_338),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(dimens.spaceSm))

            Text(
                text = message,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(dimens.spaceXl))

            Button(onClick = onRetry) {
                Text(stringResource(R.string.ui_text_023))
            }

            Spacer(modifier = Modifier.height(dimens.spaceMd))

            OutlinedButton(onClick = onChangePaymentMethod) {
                Text(stringResource(R.string.ui_text_339))
            }

            Spacer(modifier = Modifier.height(dimens.spaceMd))

            TextButton(onClick = onBackToCheckOut) {
                Text(stringResource(R.string.ui_text_340))
            }
        }
    }
}

@Preview
@Composable
fun PaymentFailedScreenPreview() {
    EasyMartTheme {
        PaymentFailedScreen(
            message = stringResource(R.string.ui_text_341),
            onRetry = {},
            onChangePaymentMethod = {},
            onBackToCheckOut = {}
        )
    }
}
