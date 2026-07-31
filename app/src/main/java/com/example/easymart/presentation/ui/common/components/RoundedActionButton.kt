package com.example.easymart.presentation.ui.common.components
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.ui.common.expansion.pressScale

@Composable
fun RoundedActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    minWidth: Dp = 64.dp,
    minHeight: Dp = 36.dp,
    horizontalPadding: Dp = 16.dp,
    verticalPadding: Dp = 8.dp,
    cornerRadius: Dp = 12.dp,
    backGroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    textStyle: TextStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
    enabled: Boolean = true,
    alpha: Float = 1f
) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(cornerRadius)
    Button(
        onClick = onClick,
        modifier = modifier
            .pressScale(
                enabled = true,
                pressedScale = 0.86f,
                interactionSource = interactionSource
            )
            .defaultMinSize(minWidth = minWidth, minHeight = minHeight)
            .clip(shape),
        interactionSource = interactionSource,
        shape = shape,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = backGroundColor.copy(alpha = alpha),
            contentColor = contentColor,
            disabledContainerColor = backGroundColor.copy(alpha = 0.3f),
            disabledContentColor = contentColor.copy(alpha = 0.3f)
        ),
        contentPadding = PaddingValues(
            horizontal = horizontalPadding,
            vertical = verticalPadding
        )
    ) {
        Text(text = text, style = textStyle)
    }
}

@Preview(showBackground = true)
@Composable
fun RoundedActionButtonPreview() {
    EasyMartTheme {
        Surface {
            RoundedActionButton(
                text = stringResource(R.string.ui_text_252),
                onClick = {}
            )
        }
    }
}
