package com.example.easymart.presentation.ui.resultorder
import androidx.compose.ui.res.stringResource

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.R
import com.example.easymart.domain.model.PaymentResult
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun OnlinePaymentProcessingScreen(
    modifier: Modifier = Modifier,
) {
    val dimens = LocalAppDimens.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(dimens.spaceLg)
        ) {

            Icon(
                painter = painterResource(id = R.drawable.ic_credit_card),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(dimens.iconLarge)
            )

            Spacer(modifier = Modifier.height(dimens.spaceLg))

            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(dimens.spaceLg))

            Text(
                text = stringResource(R.string.ui_text_331),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(dimens.spaceSm))

            Text(
                text = stringResource(R.string.ui_text_332),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
fun OnlinePaymentProcessingScreenPreview() {
    EasyMartTheme {
        OnlinePaymentProcessingScreen()
    }
}

