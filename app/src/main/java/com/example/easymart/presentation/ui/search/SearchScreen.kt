package com.example.easymart.presentation.ui.search
import androidx.compose.ui.res.stringResource
import com.example.easymart.R

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.easymart.domain.model.Product
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.ui.search.components.SearchResultGrid
import com.example.easymart.presentation.ui.search.components.SearchTopbar
import com.example.easymart.presentation.ui.search.components.SuggestionItem

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    uiState: SearchUiState,
    onProductClick: (Product) -> Unit = {},
    onSuggestionClick: (String) -> Unit = {}
) {
    val dimens = LocalAppDimens.current
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimens.spaceLg),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimens.spaceLg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.error,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            uiState.products.isNotEmpty() -> {
                SearchResultGrid(
                    products = uiState.products,
                    onProductClick = onProductClick
                )
            }
            
            uiState.suggestions.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = dimens.spaceMd)
                ) {
                    item {
                        Text(
                            text = stringResource(R.string.ui_text_345),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = dimens.spaceSm)
                        )
                    }
                    items(uiState.suggestions) { suggestion ->
                        SuggestionItem(
                            suggestion = suggestion,
                            onClick = onSuggestionClick
                        )
                    }
                }
            }
            
            uiState.searchQuery.isNotBlank() && uiState.products.isEmpty() && !uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimens.spaceLg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.ui_text_346),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    EasyMartTheme {
        SearchScreen(
            uiState = SearchUiState(
                searchQuery = "Giày",
                suggestions = listOf(stringResource(R.string.ui_text_347), stringResource(R.string.ui_text_344), stringResource(R.string.ui_text_348))
            )
        )
    }
}
