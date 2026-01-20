package com.example.easymart.presentation.ui.orderdetail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun ShippingInfo(
    name: String?,
    addressString: String?,
    phone: String?,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(dimens.radiusMedium),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.spaceXs)
    ) {
        Column(modifier = Modifier.padding(dimens.spaceMd)) {
            Text(
                text = "Thông tin giao hàng",
                style = MaterialTheme.typography.titleMedium
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = dimens.spaceSm),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )

            Text(
                text = name ?: "Chưa có tên",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(dimens.spaceSm))
            Text(
                text = addressString ?: "Chưa có địa chỉ",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(dimens.spaceXs))
            Text(
                text = "Số điện thoại: $phone",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Preview
@Composable
fun ShippingInfoPreview() {
    EasyMartTheme {
        ShippingInfo(
            name = "Nguyễn Văn A",
            addressString = "123 Đường ABC, Phường XYZ, Quận 1, TP.HCM",
            phone = "0123456789"
        )
    }
}