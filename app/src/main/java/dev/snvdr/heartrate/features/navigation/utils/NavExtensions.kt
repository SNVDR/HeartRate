package dev.snvdr.heartrate.features.navigation.utils

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController

val NavHostController.canGoBack: Boolean
    get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED

fun NavHostController.navigateBack(route:Any,inclusive:Boolean) {
    if (canGoBack) {
        popBackStack(route,inclusive)
    }
}

fun NavHostController.navigateBack() {
    if (canGoBack) {
        popBackStack()
    }
}
