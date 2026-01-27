package com.example.easymart.presentation.navigation

import android.net.Uri
import androidx.navigation.NavController
import com.example.easymart.presentation.auth.AuthState
import com.example.easymart.presentation.common.AppEventBus
import com.example.easymart.presentation.common.ui.UiEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//hỗ trợ điều hướng yêu cầu đăng nhập nếu người dùng chưa đăng nhập
fun NavController.requireLoginThenNavigate(authState: AuthState, targetRoute: String) {
    if (authState is AuthState.LoggedIn) {
        this.navigate(targetRoute)
    } else {
        CoroutineScope(Dispatchers.Main).launch {
            AppEventBus.send(UiEvent.RequireLogin(targetRoute))
        }
    }
}