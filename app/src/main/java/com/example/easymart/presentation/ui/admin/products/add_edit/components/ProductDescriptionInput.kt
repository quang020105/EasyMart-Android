@file:OptIn(ExperimentalFoundationApi::class)
package com.example.easymart.presentation.ui.admin.products.add_edit.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment as ComposeAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.presentation.theme.EasyMartTheme

private val BorderIdle = Color(0xFFE5E7EB)
private val HintColor = Color(0xFF9CA3AF)
private val TextColor = Color(0xFF111827)

@Composable
fun ProductDescriptionInput(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    maxLength: Int = 1000,
    hint: String = "Nhập mô tả chi tiết về sản phẩm...",
) {
    var isFocused by remember { mutableStateOf(false) }

    val borderColor = if (isFocused) MaterialTheme.colorScheme.primary else BorderIdle

    Card(
        modifier = modifier
            .height(120.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = ComposeAlignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 14.dp, end = 14.dp),
                    verticalAlignment = ComposeAlignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null,
                        tint = HintColor,
                        modifier = Modifier.size(20.dp)
                    )

                    Box(
                        modifier = Modifier
                            .padding(start = 14.dp)
                            .width(1.dp)
                            .height(20.dp)
                            .background(BorderIdle)
                    )
                }

                BasicTextField(
                    value = text,
                    onValueChange = { newValue ->
                        if (newValue.length <= maxLength) {
                            onTextChange(newValue)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(end = 16.dp, top = 16.dp, bottom = 16.dp)
                        .onFocusChanged { isFocused = it.isFocused },
                    textStyle = TextStyle(
                        color = TextColor,
                        fontSize = 15.sp,
                        lineHeight = 20.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Default
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    minLines = 4,
                    maxLines = 6,
                    decorationBox = { innerTextField ->
                        Box(modifier = Modifier.fillMaxSize()) {
                            if (text.isEmpty()) {
                                Text(
                                    text = hint,
                                    color = HintColor,
                                    fontSize = 15.sp,
                                    lineHeight = 20.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            Text(
                text = "${text.length}/$maxLength",
                color = HintColor,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(ComposeAlignment.BottomEnd)
                    .padding(12.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC)
@Composable
private fun ProductDescriptionInputPreview() {
    var text by rememberSaveable { mutableStateOf("") }

    EasyMartTheme {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                ProductDescriptionInput(
                    text = text,
                    onTextChange = { text = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}