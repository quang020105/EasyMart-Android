package com.example.easymart.presentation.ui.admin.products.add_edit

sealed class AdminAddEditProductUiEvent {
    data class ShowMessage(val message: String) : AdminAddEditProductUiEvent()
    data object NavigateBack : AdminAddEditProductUiEvent()
}

