package com.example.easymart.presentation.ui.deliveryaddress.components
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun DeleteSwipeBackground(
    //onDeleteClick: () -> Unit
) {
    val dimens = LocalAppDimens.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = dimens.spaceLg),
        contentAlignment = Alignment.CenterEnd
    ) {
            Icon(
                painter = painterResource(id = com.example.easymart.R.drawable.ic_delete),
                contentDescription = stringResource(R.string.ui_text_200),
            )
    }
}
