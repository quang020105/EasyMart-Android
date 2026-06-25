package com.example.easymart.presentation.ui.admin.orders.management.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Wallet
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.utils.ChipColorScheme
import com.example.easymart.utils.colorScheme
import com.example.easymart.utils.label
import com.example.easymart.utils.toDisplayString

@Composable
fun OrderStatusChip(
    status: OrderStatus,
    modifier: Modifier = Modifier
) {
    StatusPill(
        text = status.toDisplayString() ,
        colorScheme = status.colorScheme(),
        modifier = modifier
    )
}



@Composable
fun PaymentStatusChip(
    status: PaymentStatus,
    modifier: Modifier = Modifier
) {
    StatusPill(
        text = status.label(),
        colorScheme = status.colorScheme(),
        modifier = modifier
    )
}

@Composable
fun PaymentMethodChip(
    method: PaymentMethod,
    modifier: Modifier = Modifier
) {
    val scheme = method.colorScheme()

    AssistChip(
        onClick = {},
        label = {
            Text(
                text = method.toDisplayString(),
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Wallet,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        },
        modifier = modifier,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = scheme.containerColor,
            labelColor = scheme.contentColor,
            leadingIconContentColor = scheme.contentColor
        ),
        border = BorderStroke(
            width = 1.dp,
            color = scheme.borderColor
        )
    )
}

@Composable
private fun StatusPill(
    text: String,
    colorScheme: ChipColorScheme,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = colorScheme.containerColor,
        border = BorderStroke(
            width = 1.dp,
            color = colorScheme.borderColor
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 6.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(colorScheme.contentColor)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = colorScheme.contentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun ChipsPreview() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OrderStatusChip(status = OrderStatus.PROCESSING)

        PaymentStatusChip(status = PaymentStatus.UNPAID)

        PaymentMethodChip(method = PaymentMethod.COD)
    }
}