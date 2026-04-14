package com.example.easymart.presentation.ui.cart.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.easymart.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun QuantityStepper(
    quantity: Int,
    onMinusClick: () -> Unit,
    onPlusClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(999.dp)

    Surface(
        modifier = modifier,
        shape = shape,
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.65f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 3.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StepButton(
                iconRes = R.drawable.ic_minus,
                contentDescription = "Giảm số lượng",
                onClick = onMinusClick,
                size = 24.dp
            )

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                AnimatedContent(
                    targetState = quantity,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(120)) togetherWith
                                fadeOut(animationSpec = tween(120))
                    },
                    label = "quantity"
                ) { value ->
                    Text(
                        text = value.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .widthIn(min = 14.dp)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            StepButton(
                iconRes = R.drawable.ic_plus,
                contentDescription = "Tăng số lượng",
                onClick = onPlusClick,
                size = 24.dp
            )
        }
    }
}

@Composable
private fun StepButton(
    iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
    size: Dp
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor =  MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier.size(size)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = contentDescription
            )
        }
    }
}

@Preview
@Composable
fun QuantityStepperPreview() {
    QuantityStepper(
        quantity = 2,
        onMinusClick = {},
        onPlusClick = {}
    )
}