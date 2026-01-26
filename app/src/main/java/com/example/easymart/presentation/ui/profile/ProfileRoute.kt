package com.example.easymart.presentation.ui.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun ProfileRoute(
    viewModel: ProfileViewModel,
    onNavigate: (String) -> Unit,
    onLogoutSuccess: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ProfileUiEvent.Navigate -> onNavigate(event.route)
                ProfileUiEvent.LogoutSuccess -> onLogoutSuccess()
                is ProfileUiEvent.ShowMessage -> {
                    //
                }
            }
        }
    }
    ProfileScreen(
        user = uiState.user,
        onOptionClick = { viewModel.onOptionClick(it) }
    )
}