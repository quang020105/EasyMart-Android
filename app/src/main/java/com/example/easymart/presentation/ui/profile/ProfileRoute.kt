package com.example.easymart.presentation.ui.profile

import androidx.compose.runtime.Composable

@Composable
fun ProfileRoute(
    onOptionClick: (tag: String) -> Unit
){
    ProfileScreen(
        onOptionClick = onOptionClick
    )
}