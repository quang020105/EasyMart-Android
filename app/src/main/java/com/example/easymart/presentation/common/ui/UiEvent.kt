package com.example.easymart.presentation.common.ui

sealed interface UiEvent {
    data class RequireLogin(val targetRoute: String) : UiEvent
}