package com.example.easymart.presentation.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


//extension để thêm các animation
fun NavGraphBuilder.composableWithAnim(
    route: String,
    anim: NavAnim,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        enterTransition = { NavAnimations.enter(anim, this) },
        exitTransition = { NavAnimations.exit(anim, this) },
        popEnterTransition = { NavAnimations.popEnter(anim, this) },
        popExitTransition = { NavAnimations.popExit(anim, this) },
        content = content
    )
}
