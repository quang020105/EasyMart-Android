package com.example.easymart.presentation.ui.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens

@Composable
fun SuggestionItem(
    suggestion: String,
    onClick: (String) -> Unit
) {
    val dimens = LocalAppDimens.current
    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                onClick(suggestion)
                focusManager.clearFocus(true)
            })
            .background(MaterialTheme.colorScheme.background),
    ) {
        Text(
            text = suggestion,
            modifier = Modifier.padding(dimens.spaceSm),
        )
    }
}

@Preview
@Composable
fun SuggestionItemPreview() {
    EasyMartTheme {
        SuggestionItem(
            suggestion = "Suggestion",
            onClick = {}
        )
    }
}