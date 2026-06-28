package com.example.easymart.presentation.ui.category.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun CategoryEmptyState(
    modifier: Modifier = Modifier
) {
    val dimens = LocalAppDimens.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimens.space2xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimens.spaceXs)
    ) {
        Text(
            text = "Chưa có sản phẩm phù hợp",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Thử đổi danh mục, bộ lọc hoặc từ khóa tìm kiếm",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
