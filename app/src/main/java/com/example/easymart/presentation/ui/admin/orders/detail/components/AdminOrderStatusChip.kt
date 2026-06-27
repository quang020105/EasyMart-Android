package com.example.easymart.presentation.ui.admin.orders.detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.utils.colorScheme
import com.example.easymart.utils.toDisplayString

@Composable
fun AdminOrderStatusChip(
    status: OrderStatus,
    modifier: Modifier = Modifier
) {
    val colorScheme = status.colorScheme()

    AssistChip(
        onClick = {},
        modifier = modifier,
        label = {
            Text(
                text = status.toDisplayString(),
                fontWeight = FontWeight.SemiBold
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = colorScheme.containerColor,
            labelColor = colorScheme.contentColor
        ),
        border = BorderStroke(
            width = 1.dp,
            color = colorScheme.borderColor
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun AdminOrderStatusChipPreview() {
    EasyMartTheme {
        AdminOrderStatusChip(
            status = OrderStatus.PACKING
        )
    }
}