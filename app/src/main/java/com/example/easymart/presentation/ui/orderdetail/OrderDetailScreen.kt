package com.example.easymart.presentation.ui.orderdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.order.extension.toColor
import com.example.easymart.utils.toDisplayString

@Composable
fun OrderHeader(
    orderId: Long,
    createdAt: String,
    status: OrderStatus,
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current
    Card (
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(dimens.radiusMedium),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.spaceXs)
    ){
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = dimens.spaceMd, horizontal = dimens.spaceSm),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Đơn hàng #$orderId",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.height(dimens.spaceXs))
                Text(
                    text = "Đặt ngày $createdAt",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            // Badge
            Surface(
                shape = RoundedCornerShape(dimens.spaceMd),
                color = status.toColor(),
                modifier = Modifier
                    .wrapContentWidth()
                    .height(dimens.spaceLg)
                    .padding(start = dimens.spaceSm)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = dimens.spaceMd)) {
                    Text(
                        text = status.toDisplayString(),
                        style = MaterialTheme.typography.labelLarge.copy(color = Color.White, fontSize = dimens.textSmall)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun OrderHeaderPreview() {
    EasyMartTheme {
        OrderHeader(
            orderId = 123456,
            createdAt = "2024-06-15",
            status = OrderStatus.SHIPPING
        )
    }
}


