package com.example.easymart.presentation.ui.admin.orders.detail.components
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Notes
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Notes
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.presentation.theme.EasyMartTheme

@Composable
fun ShippingInfoSection(
    receiverName: String,
    receiverPhone: String,
    addressDetail: String,
    shippingNote: String?,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        SectionTitle(
            icon = Icons.Rounded.LocationOn,
            title = stringResource(R.string.label_address)
        )

        OrderDetailInfoRow(
            label = stringResource(R.string.ui_text_057),
            value = receiverName,
            icon = Icons.Rounded.Person
        )

        SectionDivider()

        OrderDetailInfoRow(
            label = stringResource(R.string.ui_text_058),
            value = receiverPhone,
            icon = Icons.Rounded.Phone
        )

        SectionDivider()

        OrderDetailMultilineInfoRow(
            label = stringResource(R.string.ui_text_059),
            value = addressDetail,
            icon = Icons.Rounded.LocationOn
        )

        if (!shippingNote.isNullOrBlank()) {
            SectionDivider()

            OrderDetailMultilineInfoRow(
                label = stringResource(R.string.ui_text_060),
                value = shippingNote,
                icon = Icons.Rounded.Notes
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShippingInfoSectionPreview() {
    EasyMartTheme {
        ShippingInfoSection(
            receiverName = "Nguyễn Văn A",
            receiverPhone = "0987 654 321",
            addressDetail = "Số 12, ngõ 45, Phường Dịch Vọng Hậu, Cầu Giấy, Hà Nội",
            shippingNote = "Giao giờ hành chính, gọi trước khi giao",
            modifier = Modifier.padding(16.dp)
        )
    }
}
