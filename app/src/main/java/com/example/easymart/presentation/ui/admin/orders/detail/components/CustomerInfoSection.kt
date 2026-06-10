package com.example.easymart.presentation.ui.admin.orders.detail.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.presentation.theme.EasyMartTheme

@Composable
fun CustomerInfoSection(
    customerName: String,
    phoneNumber: String,
    email: String?,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        SectionTitle(
            icon = Icons.Rounded.Person,
            title = "Thông tin khách hàng"
        )

        OrderDetailInfoRow(
            label = "Tên khách:",
            value = customerName,
            icon = Icons.Rounded.Person
        )

        SectionDivider()

        OrderDetailInfoRow(
            label = "Số điện thoại:",
            value = phoneNumber,
            icon = Icons.Rounded.Phone
        )

        if (!email.isNullOrBlank()) {
            SectionDivider()

            OrderDetailInfoRow(
                label = "Email:",
                value = email,
                icon = Icons.Rounded.Email
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomerInfoSectionPreview() {
    EasyMartTheme {
        CustomerInfoSection(
            customerName = "Nguyễn Văn A",
            phoneNumber = "0987 654 321",
            email = "nguyenvana@gmail.com",
            modifier = Modifier.padding(16.dp)
        )
    }
}