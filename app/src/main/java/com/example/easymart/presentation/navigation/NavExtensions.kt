package com.example.easymart.presentation.navigation

import android.net.Uri
import androidx.navigation.NavController
import com.example.easymart.presentation.auth.AuthState

//hỗ trợ điều hướng yêu cầu đăng nhập nếu người dùng chưa đăng nhập
fun NavController.requireLoginThenNavigate(authState: AuthState, targetRoute: String) {
    if (authState is AuthState.LoggedIn) {
        this.navigate(targetRoute)
    } else {
        val encoded = Uri.encode(targetRoute)
        this.navigate("${Screen.Login.route}?next=$encoded")
    }
}