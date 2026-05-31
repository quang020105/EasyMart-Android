@file:OptIn(ExperimentalFoundationApi::class)

package com.example.easymart.presentation.ui.admin.products.add_edit.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.colors.LocalAppColors
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun InputBox(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String,
    leadingIcon: ImageVector? = null,
    maxLength: Int = 100,
    height: Dp = Dp.Unspecified,
    cornerRadius: Dp = Dp.Unspecified,
    borderColor: Color = Color.Unspecified,
    focusBorderColor: Color = Color.Unspecified,
    containerColor: Color = Color.Unspecified,
    textColor: Color = Color.Unspecified,
    hintColor: Color = Color.Unspecified,
    iconColor: Color = Color.Unspecified,
    showCounter: Boolean = true,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Done
    ),
) {
    val dims = LocalAppDimens.current
    val appColors = LocalAppColors.current
    val resolvedHeight = if (height == Dp.Unspecified) dims.buttonHeight else height
    val resolvedCornerRadius =
        if (cornerRadius == Dp.Unspecified) dims.radiusLarge else cornerRadius
    val resolvedBorderColor =
        if (borderColor == Color.Unspecified) MaterialTheme.colorScheme.outlineVariant else borderColor
    val resolvedFocusBorderColor =
        if (focusBorderColor == Color.Unspecified) appColors.focusRing else focusBorderColor
    val resolvedContainerColor =
        if (containerColor == Color.Unspecified) MaterialTheme.colorScheme.surface else containerColor
    val resolvedTextColor =
        if (textColor == Color.Unspecified) appColors.textPrimary else textColor
    val resolvedHintColor =
        if (hintColor == Color.Unspecified) MaterialTheme.colorScheme.onSurfaceVariant else hintColor
    val resolvedIconColor =
        if (iconColor == Color.Unspecified) appColors.iconMuted else iconColor

    var isFocused by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(resolvedCornerRadius)
    val currentBorderColor = if (isFocused) resolvedFocusBorderColor else resolvedBorderColor
    val textStyle = MaterialTheme.typography.bodyMedium.copy(
        fontSize = dims.textBody,
        color = resolvedTextColor
    )

    Card(
        modifier = modifier.height(resolvedHeight),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = resolvedContainerColor),
        border = BorderStroke(dims.dividerThickness, currentBorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dims.spaceMd)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = resolvedIconColor,
                        modifier = Modifier.size(dims.iconMedium)
                    )

                    Spacer(modifier = Modifier.width(dims.spaceSm))

                    Box(
                        modifier = Modifier
                            .width(dims.dividerThickness)
                            .height(dims.iconMedium)
                            .background(resolvedBorderColor)
                    )

                    Spacer(modifier = Modifier.width(dims.spaceMd))
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
                        .padding(end = if (showCounter) dims.space3xl else 0.dp)
                        .onFocusChanged { isFocused = it.isFocused },
                    textStyle = textStyle,
                    keyboardOptions = keyboardOptions,
                    singleLine = singleLine,
                    minLines = minLines,
                    maxLines = maxLines,
                    cursorBrush = SolidColor(resolvedFocusBorderColor),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (value.isEmpty()) {
                                Text(
                                    text = hint,
                                    color = resolvedHintColor,
                                    fontSize = dims.textBody
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
                    color = resolvedHintColor,
                    fontSize = dims.textSmall,
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

        InputBox(
            value = text,
            onValueChange = { text = it },
            hint = "Nhập tên sản phẩm",
            leadingIcon = Icons.Default.Tag,
            maxLength = 50,
            showCounter = true
        )
    }
}