package com.example.easymart.presentation.ui.search.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction.Companion.Search
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.R
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopbar(
    onSearchClick: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    text: String,
    onSearchTextChange: (String) -> Unit = {},
    autoFocus: Boolean = false
) {
    val dimens = LocalAppDimens.current
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = "Quay lại"
                )
            }
        },
        title = {
            val keyboardController = LocalSoftwareKeyboardController.current
            val bg = MaterialTheme.colorScheme.surfaceVariant
            val onVariant = MaterialTheme.colorScheme.onSurfaceVariant
            val focusRequester = remember { FocusRequester() }
            val focusManager = LocalFocusManager.current

            //tự động focus vào ô nhập liệu khi từ màn home qua
            LaunchedEffect(Unit) {
                if(autoFocus){
                    //đợi 1 frame để layout sẵn sàng rồi focus
                    yield()
                    //delay(80)
                    focusRequester.requestFocus()
                    keyboardController?.show()
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(dimens.radiusMedium))
                    .background(bg),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        onSearchClick(text)
                        keyboardController?.hide()
                        focusManager.clearFocus(true)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = "Tìm kiếm",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                BasicTextField(
                    value = text,
                    onValueChange = onSearchTextChange,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.titleSmall,
                    keyboardOptions = KeyboardOptions(
                        imeAction = Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onSearchClick(text)
                            keyboardController?.hide()
                            focusManager.clearFocus(true)
                        }
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = dimens.spaceMd, end = dimens.space3xl)
                        .align(Alignment.CenterStart)
                        .focusRequester(focusRequester)
                        //.focusable(),
                )

                // Placeholder: chỉ hiện khi trống, canh giữa dọc, không bị che
                if (text.isEmpty()) {
                    Text(
                        text = "Tìm kiếm sản phẩm, danh mục…",
                        color = onVariant,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(start = dimens.spaceMd, end = dimens.space3xl)
                            .align(Alignment.CenterStart)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Preview
@Composable
fun SearchTopbarPreview(){
    EasyMartTheme {
        SearchTopbar(
            text = "Tìm kiếm mọi thứ"
        )
    }
}


