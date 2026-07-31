package com.example.easymart.presentation.ui.admin.orders.detail.components
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

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
            title = stringResource(R.string.ui_text_033)
        )

        OrderDetailInfoRow(
            label = stringResource(R.string.ui_text_034),
            value = customerName,
            icon = Icons.Rounded.Person
        )

        SectionDivider()

        OrderDetailInfoRow(
            label = stringResource(R.string.ui_text_035),
            value = phoneNumber,
            icon = Icons.Rounded.Phone
        )

        if (!email.isNullOrBlank()) {
            SectionDivider()

            OrderDetailInfoRow(
                label = stringResource(R.string.ui_text_036),
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
