package com.example.easymart.presentation.ui.signup

import android.widget.Space
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.common.components.AppTextField
import com.example.easymart.presentation.ui.common.components.PasswordField
import com.example.easymart.presentation.ui.common.components.RoundedActionButton

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onSignUpClick: (fullName: String, email: String, sdt: String, password: String) -> Unit = {_,_,_,_ ->},
    onNavigateToLogin: () -> Unit
) {
    val dimens = LocalAppDimens.current
    val focusManager = LocalFocusManager.current
    var fullName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var sdt by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordConfirm by rememberSaveable { mutableStateOf("") }

    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    Surface {
        Column (
            modifier = modifier.fillMaxSize().padding(all = dimens.space2xl),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(modifier = Modifier.height(dimens.space2xl))
            Text(
                text = "Đăng kí",
                modifier = Modifier.padding(bottom = dimens.spaceLg),
                style = MaterialTheme.typography.displayMedium
            )

            AppTextField(
                value = fullName,
                onValueChange = { fullName = it },
                modifier = Modifier.fillMaxWidth().padding(vertical = dimens.spaceSm),
                label = { Text("Tên đầy đủ") },
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next),
                keyBoardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            )
            AppTextField(
                value = email,
                onValueChange = { email = it},
                modifier = Modifier.fillMaxWidth().padding(vertical = dimens.spaceSm),
                label = {Text("Email")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                keyBoardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )
            AppTextField(
                value = sdt,
                onValueChange = { sdt = it},
                modifier = Modifier.fillMaxWidth().padding(vertical = dimens.spaceSm),
                label = {Text("Số điện thoại")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                keyBoardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )
            PasswordField(
                password = password,
                onPasswordChange = { password = it},
                modifier = Modifier.fillMaxWidth().padding(vertical = dimens.spaceSm),
                label = {Text("Mật khẩu")},
                isError = false,
                errorMessage = null,
                passwordVisible = passwordVisible,
                onPasswordVisibleChange = { passwordVisible = it },
                focusManager = LocalFocusManager.current
            )
            PasswordField(
                password = passwordConfirm,
                onPasswordChange = { passwordConfirm = it},
                modifier = Modifier.fillMaxWidth().padding(vertical = dimens.spaceSm),
                label = {Text("Xác nhận mật khẩu")},
                isError = false,
                errorMessage = null,
                passwordVisible = passwordVisible,
                onPasswordVisibleChange = { passwordVisible = it },
                focusManager = LocalFocusManager.current
            )
            Spacer(modifier = Modifier.height(dimens.spaceXl))
            RoundedActionButton(
                text = "Đăng kí",
                onClick = {
                    focusManager.clearFocus()
                    onSignUpClick(fullName, email, sdt, password)
                },
                modifier = Modifier.fillMaxWidth(),
                verticalPadding = dimens.spaceLg,
                textStyle = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(dimens.space2xl))
            Text(
                text = "Bạn đã có tài khoản?",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(dimens.spaceMd))
            Text(
                text = "Đăng nhập",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onNavigateToLogin() }
            )
        }
    }
}

@Preview
@Composable
fun SignUpScreenPreview() {
    EasyMartTheme {
        SignUpScreen(
            modifier = Modifier.fillMaxSize(),
            onSignUpClick = { fullName, email, sdt, password ->
                // Handle sign up click
                println("Sign up clicked with fullName: $fullName, email: $email, sdt: $sdt, password: $password")
             },
            onNavigateToLogin = { }
        )
    }
}
