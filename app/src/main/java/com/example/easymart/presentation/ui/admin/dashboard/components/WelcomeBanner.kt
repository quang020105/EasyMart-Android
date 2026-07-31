@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.easymart.presentation.ui.admin.dashboard.components
import androidx.compose.ui.res.stringResource

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.easymart.R

@Composable
fun WelcomeBanner(
    userName: String,
    subtitle: String,
    illustrationPainter: Painter? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(24.dp)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 148.dp)
                .padding(20.dp)
        ) {
            BannerDecor()

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.ui_text_027, userName),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF101828)
                        )
                    )

                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color(0xFF667085),
                            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                        )
                    )
                }

                Spacer(modifier = Modifier.size(16.dp))

                if (illustrationPainter != null) {
                    Image(
                        painter = illustrationPainter,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(124.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(124.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFF2F4F7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.ui_text_028),
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = Color(0xFF98A2B3)
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BoxScope.BannerDecor() {
    Box(
        modifier = Modifier
            .align(Alignment.CenterEnd)
            .size(130.dp)
            .background(
                color = Color(0xFFEEF2FF),
                shape = CircleShape
            )
    )

    Box(
        modifier = Modifier
            .align(Alignment.CenterEnd)
            .padding(end = 28.dp, top = 10.dp)
            .size(86.dp)
            .background(
                color = Color(0xFFDDE7FF),
                shape = CircleShape
            )
    )
}

@Preview(showBackground = true)
@Composable
private fun WelcomeBannerPreview() {
    MaterialTheme {
        WelcomeBanner(
            userName = "Admin",
            subtitle = stringResource(R.string.ui_text_005),
            illustrationPainter = painterResource(id = R.drawable.ic_online_shop)
        )
    }
}
