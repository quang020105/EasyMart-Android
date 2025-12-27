package com.example.easymart.presentation.ui.common.expansion

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.pressScale(
    enabled: Boolean = true,
    pressedScale: Float = 0.98f,
    interactionSource: MutableInteractionSource = MutableInteractionSource()
): Modifier = composed {
    val isPressed by interactionSource.collectIsPressedAsState()
    val target = if (enabled && isPressed) pressedScale else 1f
    val scaleAnim by animateFloatAsState(
        targetValue = target,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 600f)
    )

    this
        .graphicsLayer {
            scaleX = scaleAnim
            scaleY = scaleAnim
        }
}