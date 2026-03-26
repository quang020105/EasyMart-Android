package com.example.easymart.presentation.ui

import android.net.Uri
import android.os.Bundle
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.easymart.presentation.theme.EasyMartTheme
import com.example.easymart.presentation.ui.main.AppScaffold
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var startDeepLink by mutableStateOf<Uri?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        startDeepLink = intent?.data
        setContent {
            val navController = rememberNavController()
            EasyMartTheme {
                AppScaffold(
                    navController = navController,
                    startDeepLink = startDeepLink
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        startDeepLink = intent.data
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EasyMartTheme {
        AppScaffold(navController = rememberNavController(), startDeepLink = null)
    }
}