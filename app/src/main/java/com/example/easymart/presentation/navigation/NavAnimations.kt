package com.example.easymart.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut

object NavAnimations {

    private const val DURATION = 280

    //khi màn hình xuất hiện
    fun enter(anim: NavAnim, scope: AnimatedContentTransitionScope<*>)
            : EnterTransition? =
        when (anim) {
            NavAnim.HORIZONTAL -> scope.slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(DURATION)
            ) + fadeIn(tween(DURATION))

            NavAnim.MODAL -> scope.slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Up,
                tween(DURATION)
            ) + fadeIn(tween(DURATION))

            NavAnim.FADE -> fadeIn(tween(440))
            NavAnim.NONE -> null
        }

    //khi màn hình bị màn hình khác đè lên
    fun exit(anim: NavAnim, scope: AnimatedContentTransitionScope<*>)
            : ExitTransition? =
        when (anim) {
            NavAnim.HORIZONTAL -> scope.slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(DURATION)
            ) + fadeOut(tween(DURATION))

            NavAnim.MODAL -> scope.slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Down,
                tween(DURATION)
            ) + fadeOut(tween(DURATION))

            NavAnim.FADE -> fadeOut(tween(400))
            NavAnim.NONE -> null
        }

    //khi màn hình xuất hiện trở lại (khi popBackStack)
    fun popEnter(anim: NavAnim, scope: AnimatedContentTransitionScope<*>)
            : EnterTransition? =
        when (anim) {
            NavAnim.HORIZONTAL -> scope.slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(DURATION)
            ) + fadeIn(tween(DURATION))

            NavAnim.MODAL -> scope.slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Down,
                tween(DURATION)
            ) + fadeIn(tween(DURATION))

            NavAnim.FADE -> fadeIn(tween(400))
            NavAnim.NONE -> null
        }

    //khi màn hình bị đóng (khi popBackStack)
    fun popExit(anim: NavAnim, scope: AnimatedContentTransitionScope<*>)
            : ExitTransition? =
        when (anim) {
            NavAnim.HORIZONTAL -> scope.slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(DURATION)
            ) + fadeOut(tween(DURATION))

            NavAnim.MODAL -> scope.slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Up,
                tween(DURATION)
            ) + fadeOut(tween(DURATION))

            NavAnim.FADE -> fadeOut(tween(400))
            NavAnim.NONE -> null
        }
}