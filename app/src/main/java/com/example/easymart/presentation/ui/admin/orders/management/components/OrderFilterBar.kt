package com.example.easymart.presentation.ui.admin.orders.management.components
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.utils.ChipColorScheme
import com.example.easymart.utils.colorScheme
import com.example.easymart.utils.toDisplayString

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OrderFilterBar(
    selectedOrderStatus: OrderStatus?,
    selectedPaymentMethod: PaymentMethod?,
    selectedPaymentStatus: PaymentStatus?,
    onOrderStatusSelected: (OrderStatus?) -> Unit,
    onPaymentMethodSelected: (PaymentMethod?) -> Unit,
    onPaymentStatusSelected: (PaymentStatus?) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = stringResource(R.string.ui_text_070),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            FilterGroupTitle(text = stringResource(R.string.ui_text_071))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                OrderStatus.entries.forEach { status ->
                    SelectableFilterChip(
                        text = status.toDisplayString(),
                        selected = selectedOrderStatus == status,
                        colorScheme = status.colorScheme(),
                        onClick = {
                            onOrderStatusSelected(
                                if (selectedOrderStatus == status) null else status
                            )
                        }
                    )
                }
            }

            FilterGroupTitle(text = stringResource(R.string.label_payments))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                PaymentMethod.entries.forEach { method ->
                    SelectableFilterChip(
                        text = method.toDisplayString(),
                        selected = selectedPaymentMethod == method,
                        colorScheme = method.colorScheme(),
                        onClick = {
                            onPaymentMethodSelected(
                                if (selectedPaymentMethod == method) null else method
                            )
                        }
                    )
                }
            }

            FilterGroupTitle(text = stringResource(R.string.ui_text_072))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                PaymentStatus.entries.forEach { status ->
                    SelectableFilterChip(
                        text = status.toDisplayString(),
                        selected = selectedPaymentStatus == status,
                        colorScheme = status.colorScheme(),
                        onClick = {
                            onPaymentStatusSelected(
                                if (selectedPaymentStatus == status) null else status
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterGroupTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF334155)
    )
}

@Composable
private fun SelectableFilterChip(
    text: String,
    selected: Boolean,
    colorScheme: ChipColorScheme,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = text,
                fontWeight = FontWeight.SemiBold
            )
        },
        modifier = modifier,
        leadingIcon = {
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(colorScheme.contentColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        tint = Color.White
                    )
                }
            }
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.White,
            labelColor = colorScheme.contentColor,
            selectedContainerColor = colorScheme.containerColor,
            selectedLabelColor = colorScheme.contentColor
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = colorScheme.borderColor,
            selectedBorderColor = colorScheme.borderColor,
            borderWidth = 1.dp,
            selectedBorderWidth = 1.dp
        )
    )
}

@Preview
@Composable
fun OrderFilterBarPreview() {
    OrderFilterBar(
        selectedOrderStatus = OrderStatus.PACKING,
        selectedPaymentMethod = PaymentMethod.COD,
        selectedPaymentStatus = PaymentStatus.PAID,
        onOrderStatusSelected = {},
        onPaymentMethodSelected = {},
        onPaymentStatusSelected = {}
    )
}
