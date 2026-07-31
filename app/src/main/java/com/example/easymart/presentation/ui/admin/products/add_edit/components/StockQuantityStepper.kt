package com.example.easymart.presentation.ui.admin.products.add_edit.components
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.presentation.theme.EasyMartTheme

@Composable
fun StockQuantityStepper(
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier = Modifier,
    minQuantity: Int = 0,
    maxQuantity: Int = 9999
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 1.dp,
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant
        ),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            FilledTonalIconButton(
                onClick = onDecrease,
                enabled = quantity > minQuantity,
                modifier = Modifier.size(42.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Remove,
                    contentDescription = stringResource(R.string.ui_text_136)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            OutlinedTextField(
                value = quantity.toString(),
                onValueChange = { value ->
                    val number = value.toIntOrNull()

                    if (number != null &&
                        number in minQuantity..maxQuantity
                    ) {
                        onQuantityChange(number)
                    }

                    if (value.isEmpty()) {
                        onQuantityChange(0)
                    }
                },
                modifier = Modifier.width(100.dp),
                singleLine = true,
                textStyle = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            FilledTonalIconButton(
                onClick = onIncrease,
                enabled = quantity < maxQuantity,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.ui_text_137)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StockQuantityStepperPreview() {
    EasyMartTheme {
        StockQuantityStepper(
            quantity = 5,
            onIncrease = {},
            onDecrease = {},
            onQuantityChange = {}
        )
    }
}
