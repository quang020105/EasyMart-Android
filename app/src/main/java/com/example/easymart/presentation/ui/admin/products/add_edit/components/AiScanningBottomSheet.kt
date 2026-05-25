package com.example.easymart.presentation.ui.admin.products.add_edit.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easymart.presentation.theme.EasyMartTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiScanningBottomSheet(
    visible: Boolean,
    selectedImagePainter: Painter?,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!visible) return

    ModalBottomSheet(
        onDismissRequest = {},
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = modifier
    ) {
        AiScanningSheetContent(
            selectedImagePainter = selectedImagePainter,
            onCancelClick = onCancelClick
        )
    }
}

@Composable
fun AiScanningSheetContent(
    selectedImagePainter: Painter?,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Đang quét bằng AI...",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (selectedImagePainter != null) {
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Image(
                    painter = selectedImagePainter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                        )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            strokeWidth = 4.dp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "AI đang phân tích ảnh bạn chọn,\nvui lòng chờ trong giây lát.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
        ) {
            Text(
                text = "Đang nhận diện sản phẩm • Phân tích ảnh • Trích xuất dữ liệu",
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 10.dp
                ),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        OutlinedButton(
            onClick = onCancelClick,
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant
            ),
            contentPadding = PaddingValues(
                horizontal = 28.dp,
                vertical = 14.dp
            )
        ) {
            Text(
                text = "Hủy quét",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun AiScanningSheetContentPreview() {
    EasyMartTheme {
        Card(
            shape = RoundedCornerShape(28.dp)
        ) {
            AiScanningSheetContent(
                selectedImagePainter = ColorPainter(Color(0xFFEAEAEA)),
                onCancelClick = {}
            )
        }
    }
}