package com.example.easymart.presentation.ui.auth.forgot_password
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.common.components.AppTextField
import com.example.easymart.presentation.ui.common.components.RoundedActionButton
import com.example.easymart.presentation.ui.common.components.ViewLoading

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    email: String = "",
    uiState: ForgotPasswordUiState = ForgotPasswordUiState.Idle,
    onEmailChange: (String) -> Unit = {},
    onSubmitClick: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    val dimens = LocalAppDimens.current
    val isLoading = uiState == ForgotPasswordUiState.Loading
    val errorMessage = (uiState as? ForgotPasswordUiState.Error)?.message
    val isSuccess = uiState == ForgotPasswordUiState.Success

    Surface {
        if (isLoading) {
            ViewLoading(modifier = modifier)
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(all = dimens.space2xl),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(dimens.space3xl))
                Text(
                    text = stringResource(R.string.ui_text_184),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(dimens.spaceMd))
                Text(
                    text = stringResource(R.string.ui_text_185),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = dimens.spaceMd)
                )
                Spacer(modifier = Modifier.height(dimens.spaceXl))

                AppTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimens.spaceSm),
                    label = { Text(stringResource(R.string.ui_text_186)) },
                    isError = errorMessage != null,
                    errorMessage = errorMessage
                )

                if (isSuccess) {
                    Text(
                        text = stringResource(R.string.ui_text_187),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = dimens.spaceSm)
                    )
                }

                Spacer(modifier = Modifier.height(dimens.spaceXl))
                RoundedActionButton(
                    text = stringResource(R.string.ui_text_188),
                    onClick = onSubmitClick,
                    modifier = Modifier.fillMaxWidth(),
                    verticalPadding = dimens.spaceLg,
                    textStyle = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(dimens.space2xl))
                Text(
                    text = stringResource(R.string.ui_text_189),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(dimens.spaceMd))
                Text(
                    text = stringResource(R.string.label_login),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
        }
    }
}

@Preview
@Composable
fun ForgotPasswordScreenPreview() {
    EasyMartTheme {
        ForgotPasswordScreen(
            modifier = Modifier.fillMaxSize(),
            uiState = ForgotPasswordUiState.Idle
        )
    }
}

