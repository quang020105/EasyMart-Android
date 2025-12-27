package com.example.easymart.presentation.ui.common.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.R
import com.example.easymart.presentation.theme.EasyMartTheme

@Composable
fun PasswordField(
    password: String,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = { Text(text = "Mật khẩu") },
    placeholder: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    passwordVisible: Boolean? = null, //kiểm soát việc hiện thị mật khẩu từ cha
    onPasswordVisibleChange: ((Boolean) -> Unit)? = null,
    focusManager: FocusManager = LocalFocusManager.current
) {
    //nếu không truyền passwordVisible và onPasswordVisibleChange từ cha thì tự quản lý cục bộ
    val internalVisible = rememberSaveable { mutableStateOf(false) }
    val visible = passwordVisible ?: internalVisible.value
    val onVisibilityChange = onPasswordVisibleChange ?: { internalVisible.value = it }

    AppTextField(
        value = password,
        onValueChange = onPasswordChange,
        modifier = modifier,
        label = label,
        isError = isError,
        errorMessage = errorMessage,
        placeholder = placeholder,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
        keyBoardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        trailingIcon = {
            IconButton(onClick = { onVisibilityChange(!visible)}) {
                Icon(
                    painter = painterResource(id = if(visible) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                    contentDescription = if(visible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                )
            }
        },
        visualTransformation = if(visible) VisualTransformation.None else PasswordVisualTransformation()
    )
}

@Preview
@Composable
fun PasswordFieldPreview() {
   EasyMartTheme {
       PasswordField(
           password = "",
           onPasswordChange = {}
       )
   }
}