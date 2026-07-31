package com.example.easymart.presentation.ui.deliveryaddress.components
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.domain.model.Address


@Composable
fun AddressCard(
    modifier: Modifier = Modifier,
    address: Address,
    onEditClick: (() -> Unit)? = null,
    onCLick: (() -> Unit)? = null
) {
    val dimens = LocalAppDimens.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimens.spaceXs)
            .clickable{
                onCLick?.invoke()
            },
        shape = RoundedCornerShape(dimens.radiusLarge),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(dimens.radiusLarge)
                )
                .padding(all = dimens.spaceLg)
        ) {

            // ----- TAG + EDIT ICON -----
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {

                if (address.tagName != null) {
                    Box(
                        modifier = Modifier
                            .wrapContentWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                            .padding(horizontal = dimens.spaceSm, vertical = dimens.spaceXs),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = address.tagName ?: "",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(0.dp))
                }

                IconButton(
                    onClick = { onEditClick?.invoke() },
                    modifier = Modifier
                        .size(dimens.iconSmall)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Icon(
                        painter = painterResource(id = com.example.easymart.R.drawable.ic_edit),
                        contentDescription = stringResource(R.string.ui_text_259),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // -------- NAME + PHONE --------
            Row(
                modifier = Modifier
                    .padding(vertical = dimens.spaceXs)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = address.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
                VerticalDivider(
                    modifier = Modifier
                        .padding(horizontal = dimens.spaceSm)
                        .height(16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    thickness = 1.dp
                )
                Text(
                    text = address.phone,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            Text(
                text = address.addressString,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = dimens.spaceXs)
            )
        }
    }
}



@Preview(showBackground = false)
@Composable
private fun AddressCardPreview() {
    EasyMartTheme {
        AddressCard(
            address = Address(
                name = "Nguyễn Văn A",
                detailAddress = "123 Đường ABC, Phường XYZ",
                addressString = "Quận 1, TP. Hồ Chí Minh",
                isDefault = true,
                phone = "0367985485"
            )
        )
    }
}
