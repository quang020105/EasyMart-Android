package com.example.easymart.presentation.ui.auth.signup
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.common.components.AppTextField
import com.example.easymart.presentation.ui.common.components.PasswordField
import com.example.easymart.presentation.ui.common.components.RoundedActionButton
import com.example.easymart.presentation.ui.common.components.ViewLoading

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    formState: SignUpFormState = SignUpFormState(),
    uiState: SignUpUiState = SignUpUiState.Idle,
    onFullNameChange: (String) -> Unit = {},
    onEmailChange: (String) -> Unit = {},
    onPhoneChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onConfirmPasswordChange: (String) -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onNavigateToLogin: () -> Unit
) {
    val dimens = LocalAppDimens.current
    val focusManager = LocalFocusManager.current
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    Surface {
        if (uiState == SignUpUiState.Loading) {
            ViewLoading(modifier = modifier)

        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(all = dimens.space2xl),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(dimens.space2xl))
                Text(
                    text = stringResource(R.string.ui_text_193),
                    modifier = Modifier.padding(bottom = dimens.spaceLg),
                    style = MaterialTheme.typography.displayMedium
                )

                AppTextField(
                    value = formState.fullName,
                    onValueChange = { onFullNameChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimens.spaceSm),
                    label = { Text(stringResource(R.string.ui_text_194)) },
                    isError = formState.fullNameError != null,
                    errorMessage = formState.fullNameError,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    keyBoardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    }),
                )
                AppTextField(
                    value = formState.email,
                    onValueChange = { onEmailChange(it) },
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
                AppTextField(
                    value = formState.phone,
                    onValueChange = { onPhoneChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimens.spaceSm),
                    label = { Text(stringResource(R.string.ui_text_195)) },
                    isError = formState.phoneError != null,
                    errorMessage = formState.phoneError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
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
                    onPasswordChange = { onPasswordChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimens.spaceSm),
                    label = { Text(stringResource(R.string.ui_text_190)) },
                    isError = formState.passwordError != null,
                    errorMessage = formState.passwordError,
//                passwordVisible = passwordVisible,
//                onPasswordVisibleChange = { passwordVisible = it },
                    focusManager = LocalFocusManager.current
                )
                PasswordField(
                    password = formState.confirmPassword,
                    onPasswordChange = { onConfirmPasswordChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimens.spaceSm),
                    label = { Text(stringResource(R.string.ui_text_196)) },
                    isError = formState.confirmPasswordError != null,
                    errorMessage = formState.confirmPasswordError,
                passwordVisible = passwordVisible,
                onPasswordVisibleChange = { passwordVisible = it },
                    focusManager = LocalFocusManager.current
                )
                Spacer(modifier = Modifier.height(dimens.spaceXl))
                RoundedActionButton(
                    text = stringResource(R.string.ui_text_193),
                    onClick = {
                        focusManager.clearFocus()
                        onSignUpClick()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    verticalPadding = dimens.spaceLg,
                    textStyle = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(dimens.space2xl))
                Text(
                    text = stringResource(R.string.ui_text_197),
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
fun SignUpScreenPreview() {
    EasyMartTheme {
        SignUpScreen(
            modifier = Modifier.fillMaxSize(),
            onSignUpClick = { },
            onNavigateToLogin = { }
        )
    }
}
