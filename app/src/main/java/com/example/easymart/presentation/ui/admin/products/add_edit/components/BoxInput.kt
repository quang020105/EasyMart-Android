
@file:OptIn(ExperimentalFoundationApi::class)
package com.example.easymart.presentation.ui.admin.products.add_edit.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tag
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easymart.presentation.theme.EasyMartTheme

@Composable
fun EasyMartInputBox(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String,
    leadingIcon: ImageVector? = null,
    maxLength: Int = 100,
    height: androidx.compose.ui.unit.Dp = 56.dp,
    cornerRadius: androidx.compose.ui.unit.Dp = 14.dp,
    borderColor: Color = Color(0xFFE5E7EB),
    focusBorderColor: Color = MaterialTheme.colorScheme.primary,
    containerColor: Color = Color.White,
    textColor: Color = Color(0xFF111827),
    hintColor: Color = Color(0xFF9CA3AF),
    iconColor: Color = Color(0xFF9CA3AF),
    showCounter: Boolean = true,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Done
    ),
) {
    var isFocused by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(cornerRadius)
    val currentBorderColor = if (isFocused) focusBorderColor else borderColor

    Card(
        modifier = modifier.height(height),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, currentBorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(20.dp)
                            .background(borderColor)
                    )

                    Spacer(modifier = Modifier.width(12.dp))
                }

                BasicTextField(
                    value = value,
                    onValueChange = { newValue ->
                        if (newValue.length <= maxLength) {
                            onValueChange(newValue)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .onFocusChanged { isFocused = it.isFocused },
                    textStyle = TextStyle(
                        color = textColor,
                        fontSize = 15.sp
                    ),
                    keyboardOptions = keyboardOptions,
                    singleLine = singleLine,
                    minLines = minLines,
                    maxLines = maxLines,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            if (value.isEmpty()) {
                                Text(
                                    text = hint,
                                    color = hintColor,
                                    fontSize = 15.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            if (showCounter) {
                Text(
                    text = "${value.length}/$maxLength",
                    color = hintColor,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
    }
}

@Preview

@Composable
fun EasyMartInputBoxPreview() {
    EasyMartTheme {
        var text by rememberSaveable { mutableStateOf("") }

        EasyMartInputBox(
            value = text,
            onValueChange = { text = it },
            hint = "Nhập tên sản phẩm",
            leadingIcon = Icons.Default.Tag,
            maxLength = 50,
            showCounter = true
        )
    }
}