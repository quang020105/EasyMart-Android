package com.example.easymart.presentation.ui.admin.dashboard.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.presentation.theme.EasyMartTheme
import kotlin.math.max

@Composable
fun SparklineChart(
    values: List<Float>,
    color: Color,
    modifier: Modifier = Modifier,
    strokeWidth: androidx.compose.ui.unit.Dp = 2.dp
) {
    Canvas(modifier = modifier) {
        if (values.size < 2) return@Canvas

        val minValue = values.minOrNull() ?: 0f
        val maxValue = values.maxOrNull() ?: 0f
        val range = max(maxValue - minValue, 0.0001f)

        val stepX = size.width / (values.size - 1)

        val points = values.mapIndexed { index, value ->
            val normalized = (value - minValue) / range
            val x = index * stepX
            val y = size.height - (normalized * size.height)
            Offset(x, y)
        }

        // Đường cong chính
        val linePath = Path().apply {
            moveTo(points.first().x, points.first().y)

            for (i in 0 until points.lastIndex) {
                val p1 = points[i]
                val p2 = points[i + 1]
                val midPoint = Offset(
                    x = (p1.x + p2.x) / 2f,
                    y = (p1.y + p2.y) / 2f
                )
                quadraticTo(
                    p1.x,
                    p1.y,
                    midPoint.x,
                    midPoint.y
                )
            }

            lineTo(points.last().x, points.last().y)
        }

        // Vùng phía dưới đường cong
        val fillPath = Path().apply {
            moveTo(points.first().x, size.height)
            lineTo(points.first().x, points.first().y)

            for (i in 0 until points.lastIndex) {
                val p1 = points[i]
                val p2 = points[i + 1]
                val midPoint = Offset(
                    x = (p1.x + p2.x) / 2f,
                    y = (p1.y + p2.y) / 2f
                )
                quadraticTo(
                    p1.x,
                    p1.y,
                    midPoint.x,
                    midPoint.y
                )
            }

            lineTo(points.last().x, size.height)
            close()
        }

        // Tô màu nhạt dần phía dưới
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    color.copy(alpha = 0.28f),
                    color.copy(alpha = 0.08f),
                    Color.Transparent
                )
            )
        )

        // Vẽ đường cong phía trên
        drawPath(
            path = linePath,
            color = color,
            style = Stroke(
                width = strokeWidth.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Chấm cuối đường
        val last = points.last()
        drawCircle(
            color = color,
            radius = 2.2.dp.toPx(),
            center = last
        )
    }
}

@Preview
@Composable
fun SparklineChartPreview() {
    val sampleData = listOf(10f, 20f, 15f, 25f, 18f, 30f)
    EasyMartTheme {
        SparklineChart(
            values = sampleData,
            color = Color(0xFF4CAF50),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
    }
}