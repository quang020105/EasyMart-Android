package com.example.easymart.presentation.ui.deliveryaddress.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.R
import com.example.easymart.domain.model.Address

@Composable
fun AddressCard(
    modifier: Modifier = Modifier,
    address: Address
) {
    val dimens = LocalAppDimens.current

    Card(
        modifier = modifier
            .fillMaxWidth().border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                shape = RoundedCornerShape(dimens.radiusLarge)
            ),
        shape = RoundedCornerShape(dimens.radiusLarge),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimens.spaceLg)
        ) {
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                    .padding(horizontal = dimens.spaceSm, vertical = dimens.spaceXs),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if(address.tag) "Mặc định" else "",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }


            Row (
                modifier = Modifier
                    .padding(vertical = dimens.spaceXs)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = address.name,
                    style = MaterialTheme.typography.titleMedium,
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = address.detailAddress,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )


            // District / city
            Text(
                text = address.districtCity,
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
                districtCity = "Quận 1, TP. Hồ Chí Minh",
                tag = true,
                phone = "0367985485"
            )
        )
    }
}
