@file:OptIn(ExperimentalFoundationApi::class)

package com.example.easymart.presentation.ui.admin.products.add_edit.components
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.colors.LocalAppColors
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun ProductDescriptionInput(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "Nhập mô tả chi tiết về sản phẩm...",
    maxLength: Int = 1000,
    minLines: Int = 4,
    maxLines: Int = 8
) {

    val dims = LocalAppDimens.current
    val appColors = LocalAppColors.current

    var isFocused by remember { mutableStateOf(false) }

    val borderColor =
        if (isFocused) {
            appColors.focusRing
        } else {
            MaterialTheme.colorScheme.outlineVariant
        }

    val textStyle = MaterialTheme.typography.bodyMedium.copy(
        fontSize = dims.textBody,
        color = appColors.textPrimary
    )

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(dims.radiusLarge),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            dims.dividerThickness,
            borderColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dims.spaceMd)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null,
                        tint = appColors.iconMuted,
                        modifier = Modifier.size(dims.iconMedium)
                    )

                    Spacer(modifier = Modifier.height(dims.spaceSm))

                    Box(
                        modifier = Modifier
                            .width(dims.dividerThickness)
                            .height(48.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant)
                    )
                }

                Spacer(modifier = Modifier.width(dims.spaceMd))

                BasicTextField(
                    value = text,
                    onValueChange = { newValue ->
                        if (newValue.length <= maxLength) {
                            onTextChange(newValue)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged {
                            isFocused = it.isFocused
                        },
                    textStyle = textStyle,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Default
                    ),
                    cursorBrush = SolidColor(appColors.focusRing),
                    minLines = minLines,
                    maxLines = maxLines,
                    decorationBox = { innerTextField ->

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = dims.spaceLg),
                            contentAlignment = Alignment.TopStart
                        ) {

                            if (text.isEmpty()) {
                                Text(
                                    text = hint,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = dims.textBody
                                )
                            }

                            innerTextField()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(dims.spaceSm))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                Text(
                    text = stringResource(R.string.ui, text.length, maxLength),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = dims.textSmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductDescriptionInputPreview() {

    var text by remember {
        mutableStateOf("")
    }

    EasyMartTheme {

        Surface {

            ProductDescriptionInput(
                text = text,
                onTextChange = {
                    text = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}
