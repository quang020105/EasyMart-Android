package com.example.easymart.presentation.ui.login

import android.util.Log
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
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginClick: (email: String, sdt: String, password: String) -> Unit = {_,_,_ ->},
    onNavigateToSignUp: () -> Unit
) {
    val dimens = LocalAppDimens.current
    val focusManager = LocalFocusManager.current
    var email by rememberSaveable { mutableStateOf("") }
    var sdt by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    Surface {
        Column (
            modifier = modifier.fillMaxSize().padding(all = dimens.space2xl),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(modifier = Modifier.height(dimens.space4xl))
            Text(
                text = "Đăng nhập",
                modifier = Modifier.padding(vertical = dimens.spaceLg),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold
            )

            AppTextField(
                value = email,
                onValueChange = { email = it},
                modifier = Modifier.fillMaxWidth().padding(vertical = dimens.spaceSm),
                label = {Text("Email hoặc số điện thoại")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                keyBoardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )
            PasswordField(
                password = password,
                onPasswordChange = { password = it},
                modifier = Modifier.fillMaxWidth().padding(top = dimens.spaceSm),
                label = {Text("Mật khẩu")},
                isError = false,
                errorMessage = null,
                passwordVisible = passwordVisible,
                onPasswordVisibleChange = { passwordVisible = it },
                focusManager = LocalFocusManager.current
            )
            Text(
                text = "Quên mật khẩu?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth().padding(vertical = dimens.spaceLg, horizontal = dimens.spaceSm)
            )
            Spacer(modifier = Modifier.height(dimens.spaceMd))
            RoundedActionButton(
                text = "Đăng nhập",
                onClick = {
                    focusManager.clearFocus()
                    onLoginClick(email, sdt, password)
                },
                modifier = Modifier.fillMaxWidth(),
                verticalPadding = dimens.spaceLg,
                textStyle = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(dimens.space2xl))
            Text(
                text = "Bạn chưa có tài khoản?",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(dimens.spaceMd))
            Text(
                text = "Đăng kí",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onNavigateToSignUp() }
            )
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    EasyMartTheme {
        LoginScreen (
            modifier = Modifier.fillMaxSize(),
            onLoginClick = { email, sdt, password ->
                // Handle sign up click
                Log.d("login","Sign up clicked with fullName: email: $email, sdt: $sdt, password: $password")
            },
            onNavigateToSignUp = { }
        )
    }
}