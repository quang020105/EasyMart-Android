package com.example.easymart.presentation.ui.admin.products.add_edit.components
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.easymart.presentation.theme.EasyMartTheme

data class AiScanImageItemUi(
    val id: String,
    val title: String,
    val subtitle: String,
    val painter: Painter
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiImagePickerBottomSheet(
    visible: Boolean,
    items: List<AiScanImageItemUi>,
    selectedImageId: String?,
    onSelectImage: (String) -> Unit,
    onDismiss: () -> Unit,
    onStartScan: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!visible) return

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = modifier
    ) {
        AiImagePickerSheetContent(
            items = items,
            selectedImageId = selectedImageId,
            onSelectImage = onSelectImage,
            onDismiss = onDismiss,
            onStartScan = onStartScan
        )
    }
}

@Composable
fun AiImagePickerSheetContent(
    items: List<AiScanImageItemUi>,
    selectedImageId: String?,
    onSelectImage: (String) -> Unit,
    onDismiss: () -> Unit,
    onStartScan: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedItem = items.firstOrNull { it.id == selectedImageId }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = stringResource(R.string.ui_text_099),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            text = stringResource(R.string.ui_text_100),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.size(16.dp))

        if (items.isEmpty()) {
            EmptyAiImageListState(
                onDismiss = onDismiss
            )
            return
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 420.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items, key = { it.id }) { item ->
                AiImagePickerItem(
                    item = item,
                    selected = item.id == selectedImageId,
                    onClick = { onSelectImage(item.id) }
                )
            }
        }

        Spacer(modifier = Modifier.size(16.dp))

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        Spacer(modifier = Modifier.size(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.ui_text_101),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Button(
                onClick = onStartScan,
                enabled = selectedItem != null,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = stringResource(R.string.ui_text_102),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }

        Spacer(modifier = Modifier.size(12.dp))
    }
}

@Composable
private fun AiImagePickerItem(
    item: AiScanImageItemUi,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = if (selected) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    val borderColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = containerColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Image(
                    painter = item.painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(54.dp)
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.size(2.dp))
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            RadioButton(
                selected = selected,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun EmptyAiImageListState(
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Icon(
                imageVector = Icons.Rounded.Image,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(18.dp)
                    .size(34.dp)
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            text = stringResource(R.string.ui_text_103),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            text = stringResource(R.string.ui_text_104),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.size(16.dp))

        OutlinedButton(
            onClick = onDismiss,
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(stringResource(R.string.ui_text_076))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AiImagePickerItemPreview(){
    val sampleItem = AiScanImageItemUi(
        id = "1",
        title = stringResource(R.string.ui_text_073),
        subtitle = stringResource(R.string.ui_text_105),
        painter = ColorPainter(Color(0xFFF1F1F1))
    )

    EasyMartTheme {
        AiImagePickerItem(
            item = sampleItem,
            selected = true,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AiImagePickerSheetContentPreview() {
    val primaryImageTitle = stringResource(R.string.ui_text_073)
    val primaryImageSubtitle = stringResource(R.string.ui_text_105)
    val backImageTitle = stringResource(R.string.ui_text_106)
    val backImageSubtitle = stringResource(R.string.ui_text_107)
    val labelImageTitle = stringResource(R.string.ui_text_108)
    val labelImageSubtitle = stringResource(R.string.ui_text_109)
    val sampleItems = remember(
        primaryImageTitle,
        primaryImageSubtitle,
        backImageTitle,
        backImageSubtitle,
        labelImageTitle,
        labelImageSubtitle
    ) {
        listOf(
            AiScanImageItemUi(
                id = "1",
                title = primaryImageTitle,
                subtitle = primaryImageSubtitle,
                painter = ColorPainter(Color(0xFFF1F1F1))
            ),
            AiScanImageItemUi(
                id = "2",
                title = backImageTitle,
                subtitle = backImageSubtitle,
                painter = ColorPainter(Color(0xFFE8E8E8))
            ),
            AiScanImageItemUi(
                id = "3",
                title = labelImageTitle,
                subtitle = labelImageSubtitle,
                painter = ColorPainter(Color(0xFFE0E0E0))
            )
        )
    }

    EasyMartTheme {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            AiImagePickerSheetContent(
                items = sampleItems,
                selectedImageId = "1",
                onSelectImage = {},
                onDismiss = {},
                onStartScan = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyAiImageListStatePreview() {
    EasyMartTheme {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            EmptyAiImageListState(onDismiss = {})
        }
    }
}
