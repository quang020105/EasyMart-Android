package com.example.easymart.presentation.ui.auth.login
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.common.components.AppTextField
import com.example.easymart.presentation.ui.common.components.PasswordField
import com.example.easymart.presentation.ui.common.components.RoundedActionButton
import com.example.easymart.presentation.ui.common.components.ViewLoading

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    formState: LoginFormState = LoginFormState(),
    uiState: LoginUiState,
    onLoginClick: (email: String, sdt: String, password: String) -> Unit = { _, _, _ -> },
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit = {},
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
) {
    val dimens = LocalAppDimens.current
    val focusManager = LocalFocusManager.current
    val isBusy = uiState == LoginUiState.Loading
//    var email by rememberSaveable { mutableStateOf("") }
//    var sdt by rememberSaveable { mutableStateOf("") }
//    var password by rememberSaveable { mutableStateOf("") }
//    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    Surface {
        if (isBusy) {
            ViewLoading(modifier = modifier)

        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(all = dimens.space2xl),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(dimens.space4xl))
                Text(
                    text = stringResource(R.string.label_login),
                    modifier = Modifier.padding(vertical = dimens.spaceLg),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold
                )

                AppTextField(
                    value = formState.email,
                    onValueChange = onEmailChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimens.spaceSm),
                    label = { Text(stringResource(R.string.ui_text_186)) },
                    isError = formState.emailError != null,
                    errorMessage = formState.emailError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyBoardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    })
                )
                PasswordField(
                    password = formState.password,
                    onPasswordChange = onPasswordChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dimens.spaceSm),
                    label = { Text(stringResource(R.string.ui_text_190)) },
                    isError = formState.passwordError != null,
                    errorMessage = formState.passwordError,
                    //passwordVisible = passwordVisible,
                    //onPasswordVisibleChange = { passwordVisible = it },
                    focusManager = LocalFocusManager.current
                )
                Text(
                    text = stringResource(R.string.ui_text_191),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimens.spaceLg, horizontal = dimens.spaceSm)
                        .clickable { onNavigateToForgotPassword() }
                )
                Spacer(modifier = Modifier.height(dimens.spaceMd))
                RoundedActionButton(
                    text = stringResource(R.string.label_login),
                    onClick = {
                        focusManager.clearFocus()
                        onLoginClick(formState.email, "57478", formState.password)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    verticalPadding = dimens.spaceLg,
                    textStyle = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(dimens.space2xl))
                Text(
                    text = stringResource(R.string.ui_text_192),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(dimens.spaceMd))
                Text(
                    text = stringResource(R.string.ui_text_193),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onNavigateToSignUp() }
                )
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    EasyMartTheme {
        LoginScreen(
            modifier = Modifier.fillMaxSize(),
            uiState = LoginUiState.Idle,
            onLoginClick = { email, sdt, password ->
                // Handle sign up click
                Log.d(
                    "login",
                    "Sign up clicked with fullName: email: $email, sdt: $sdt, password: $password"
                )
            },
            onNavigateToSignUp = { },
            onNavigateToForgotPassword = { }
        )
    }
}
