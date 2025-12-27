package com.example.easymart.presentation.ui.deliveryaddress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.domain.model.Address
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.deliveryaddress.components.AddressCard

@Composable
fun DeliveryAddressScreen(
    modifier: Modifier = Modifier,
    address: List<Address> = emptyList(),
    onAddressClick: (Address) -> Unit = {},
    onAddressEditClick: (addressId: Int) -> Unit = {},
    onAddressAddClick: () -> Unit = {}
) {
    val dimens = LocalAppDimens.current
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(all = dimens.spaceLg),
    ) {
        item {
            Text(
                text = "Địa chỉ",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(dimens.spaceSm)
            )
        }

        items(address) { address ->
            AddressCard(
                address = address,
                onEditClick = { onAddressEditClick(address.id) }
            )
        }

        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onAddressAddClick,
                    modifier = Modifier.padding(vertical = dimens.spaceMd),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                ) {
                    Icon(
                        painter = painterResource(id = com.example.easymart.R.drawable.ic_add),
                        contentDescription = "Biểu tượng thêm",
                        modifier = Modifier
                            .height(dimens.iconMedium)
                            .width(dimens.iconMedium)
                    )
                    Text(
                        text = "Thêm địa chỉ mới",
                        modifier = Modifier.padding(start = dimens.spaceSm)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun DeliveryAddressScreenPreview() {
    EasyMartTheme {
        DeliveryAddressScreen(
            address = listOf(
                Address(
                    isDefault = true,
                    name = "Nguyễn Văn A",
                    phone = "0123456789",
                    detailAddress = "123 Đường A, Phường B, Quận",
                    districtCity = " Quận C, TP. HCM"
                ),
                Address(
                    name = "Trần Thị B",
                    phone = "0987654321",
                    detailAddress = "456 Đường X, Phường Yên xá",
                    districtCity = "Quận Hà Đông, Hà Nội"
                )
            )
        )
    }
}