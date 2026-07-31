package com.example.easymart.presentation.ui.deliveryaddress
import androidx.compose.ui.res.stringResource

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.AlertDialog
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DismissDirection
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.SwipeToDismiss
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TextButton
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.R
import com.example.easymart.domain.model.Address
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.deliveryaddress.components.AddressCard
import com.example.easymart.presentation.ui.deliveryaddress.components.DeleteSwipeBackground

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeliveryAddressScreen(
    modifier: Modifier = Modifier,
    addresses: List<Address> = emptyList(),
    onAddressClick: (Address) -> Unit = {},
    onAddressEditClick: (addressId: Int) -> Unit = {},
    onAddressDeleteClick: (addressId: Int) -> Unit = {},
    onAddressAddClick: () -> Unit = {}
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deleteAddressId by remember { mutableStateOf<Int?>(null) }
    val dimens = LocalAppDimens.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(all = dimens.spaceLg),
    ) {
        item {
            Text(
                text = stringResource(R.string.ui_text_262),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(dimens.spaceSm)
            )
        }

//        items(addresses) { address ->
//            AddressCard(
//                address = address,
//                onEditClick = { onAddressEditClick(address.id) }
//            )
//        }

        items(items = addresses, key = { it.id }) { address ->
            val dismissState = rememberDismissState(
                confirmStateChange = { dismissValue ->
                    if (dismissValue == DismissValue.DismissedToStart) {
                        deleteAddressId = address.id
                        showDeleteDialog = true
                        false //không cho dismiss luôn
                    } else {
                        true
                    }
                }
            )


            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                background = {
                    DeleteSwipeBackground(
                    )
                },
                dismissContent = {
                    AddressCard(
                        address = address,
                        onEditClick = { onAddressEditClick(address.id) },
                        onCLick = { onAddressClick(address) }
                    )
                }
            )

//            SwipeAddressItem(
//                address = address,
//                onEditClick = { onAddressEditClick(address.id) },
//                onDeleteClick = {
//                    deleteAddressId = address.id
//                    showDeleteDialog = true
//                }
//            )
        }

        item {
//            Box(
//                modifier = Modifier.fillMaxWidth(),
//                contentAlignment = Alignment.Center
//            ) {
//                Button(
//                    onClick = onAddressAddClick,
//                    modifier = Modifier.padding(vertical = dimens.spaceMd),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
//                        contentColor = MaterialTheme.colorScheme.onPrimary
//                    ),
//                ) {
//                    Icon(
//                        painter = painterResource(id = com.example.easymart.R.drawable.ic_add),
//                        contentDescription = "Biểu tượng thêm",
//                        modifier = Modifier
//                            .height(dimens.iconMedium)
//                            .width(dimens.iconMedium)
//                    )
//                    Text(
//                        text = "Thêm địa chỉ mới",
//                        modifier = Modifier.padding(start = dimens.spaceSm)
//                    )
//                }
//            }

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                FilledTonalButton(
                    onClick = onAddressAddClick,
                    modifier = Modifier.padding(top = dimens.spaceSm),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = stringResource(R.string.ui_text_227)
                    )
                    Spacer(modifier = Modifier.width(dimens.spaceSm))
                    Text(text = stringResource(R.string.ui_text_227))
                }
            }
        }
    }

    if (showDeleteDialog && deleteAddressId != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.ui_text_263)) },
            text = { Text(stringResource(R.string.ui_text_264)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onAddressDeleteClick(deleteAddressId!!)
                        showDeleteDialog = false
                        deleteAddressId = null
                    }
                ) {
                    Text(stringResource(R.string.ui_text_200), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        deleteAddressId = null
                    }
                ) {
                    Text(stringResource(R.string.ui_text_101))
                }
            }
        )
    }


}

@Preview
@Composable
fun DeliveryAddressScreenPreview() {
    EasyMartTheme {
        DeliveryAddressScreen(
            addresses = listOf(
                Address(
                    isDefault = true,
                    name = "Nguyễn Văn A",
                    phone = "0123456789",
                    detailAddress = "123 Đường A, Phường B, Quận",
                    addressString = " Quận C, TP. HCM"
                ),
                Address(
                    name = "Trần Thị B",
                    phone = "0987654321",
                    detailAddress = "456 Đường X, Phường Yên xá",
                    addressString = "Quận Hà Đông, Hà Nội"
                )
            )
        )
    }
}
