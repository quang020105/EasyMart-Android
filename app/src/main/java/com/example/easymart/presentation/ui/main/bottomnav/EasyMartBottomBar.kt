package com.example.easymart.presentation.ui.main.bottomnav

import android.annotation.SuppressLint
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun EasyMartBottomBar(
    items: List<BottomNavItem>,
    currentGraphRoute: String?,
    onItemClick: (BottomNavItem) -> Unit
) {
    val selectedIndex = items.indexOfFirst { it.graphRoute == currentGraphRoute }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val tabWidth = screenWidth / items.size
    val indicatorWidth = 40.dp
    //chuyển động mượt của indicator
    val indicatorOffset by animateDpAsState(
        targetValue = tabWidth * selectedIndex + (tabWidth - indicatorWidth) / 2,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(64.dp)
            .background(MaterialTheme.colorScheme.surface)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
    ) {
        // Indicator trượt
        if (selectedIndex >= 0) {
            Box(
                modifier = Modifier
                    .offset(
                        x = indicatorOffset
                    )
                    .width(indicatorWidth)
                    .height(4.dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(50)
                    )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                Box(
                    modifier = Modifier
                        .width(tabWidth),
                    contentAlignment = Alignment.Center
                ) {
                    BottomNavItemView(
                        item = item,
                        selected = item.graphRoute == currentGraphRoute,
                        onClick = { onItemClick(item) }
                    )
                }
            }
        }
    }
}
