package com.example.easymart.presentation.ui.main.bottomnav

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavItemView(
    item: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    // giá trị phóng to
    val scale by animateFloatAsState(
        if (selected) 1.20f else 1f
    )

    // giá trị dịch chuyển theo trục Y
    val offsetY by animateDpAsState(
        if (selected) (-6).dp else 0.dp
    )

    // độ mờ của text
    val textAlpha by animateFloatAsState(
        if (selected) 1f else 0.6f,
        label = ""
    )


    Column(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationY = offsetY.toPx()
            }
            .clickable(
                indication = null, // bỏ ripple vuông
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            }
            .padding(start = 16.dp, end = 16.dp, top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(item.icon),
            contentDescription = item.label,
            tint = if (selected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = item.label,
            modifier = Modifier.graphicsLayer { alpha = textAlpha },
            color = if (selected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
