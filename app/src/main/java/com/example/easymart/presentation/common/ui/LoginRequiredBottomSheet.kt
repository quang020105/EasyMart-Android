package com.example.easymart.presentation.common.ui
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easymart.presentation.theme.EasyMartTheme

@Composable
fun LoginRequiredBottomSheet(
    onLoginClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.ui_text_001), style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(8.dp))

        Text(
            "Bạn cần đăng nhập để sử dụng chức năng này",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(Modifier.height(24.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onLoginClick
        ) {
            Text(stringResource(R.string.label_login))
        }

        TextButton(onClick = onDismiss) {
            Text(stringResource(R.string.ui_text_002))
        }
    }
}

@Preview
@Composable
fun LoginRequiredBottomSheetPreview() {
    EasyMartTheme {
        LoginRequiredBottomSheet(
            onLoginClick = {},
            onDismiss = {}
        )
    }
}
