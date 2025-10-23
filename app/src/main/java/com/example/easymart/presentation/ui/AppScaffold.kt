package com.example.easymart.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.easymart.R
import com.example.easymart.presentation.navigation.AppNavGraph
import com.example.easymart.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(navController: NavHostController) {
    // theo dõi backstack entry hiện tại
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    // lấy route hiện tại
    val currentRoute = navBackStackEntry?.destination?.route

    //những màn cần ẩn cả topBar và bottomBar
    val topBottomHiddenRoutes = setOf(
        Screen.Splash.route,
        Screen.Login.route,
        Screen.SignUp.route
    )

    val bottomNavItems = listOf(
        BottomNavItem(
            "home",
            "Trang chủ",
            icon = R.drawable.ic_home,
            graphRoute = Screen.HomeGraph.route
        ),
        BottomNavItem(
            "category",
            "Danh mục",
            icon = R.drawable.ic_category,
            graphRoute = Screen.CategoryGraph.route
        ),
        BottomNavItem(
            "search",
            "Tìm kiếm",
            icon = R.drawable.ic_search,
            graphRoute = Screen.SearchGraph.route
        ),
        BottomNavItem(
            "profile",
            "Cá nhân",
            icon = R.drawable.ic_person,
            graphRoute = Screen.ProfileGraph.route
        )
    )

    val showTopBar = currentRoute !in topBottomHiddenRoutes
    val showBottomBar = currentRoute != null && bottomNavItems.any { item ->
        currentRoute.startsWith(item.graphRoute)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (showTopBar) {
                val isRootTopLevel = currentRoute != null && bottomNavItems.any { item ->
                    currentRoute == item.graphRoute || currentRoute.startsWith(item.graphRoute)
                }
                TopAppBar(
                    title = { Text("") },
                    navigationIcon = {
                        if (isRootTopLevel) {
                            Text(text = "EasyMart")
                        } else {
                            IconButton(
                                onClick = { navController.navigateUp() }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_back),
                                    contentDescription = null
                                )
                            }
                        }
                    },

                    )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    val currentGraphRoute = bottomNavItems.find { item ->
                        currentRoute?.startsWith(item.graphRoute) == true
                    }?.graphRoute
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    painter = painterResource(id = item.icon),
                                    contentDescription = item.label
                                )
                            },
                            label = { Text("") },
                            onClick = {
                                navController.navigate(item.graphRoute) {
                                    launchSingleTop = true
                                    restoreState = true
//                                    popUpTo(Screen.HomeGraph.route) {
//                                        saveState = true
//                                    }
                                }
                            },
                            selected = currentGraphRoute == item.graphRoute
                        )
                    }
                }

            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)){
            AppNavGraph(navController = navController, modifier = Modifier.fillMaxSize())
        }
    }
}

//các thể hiện của BottomNavItem
data class BottomNavItem(
    val id: String,
    val label: String,
    val icon: Int,
    val graphRoute: String
)