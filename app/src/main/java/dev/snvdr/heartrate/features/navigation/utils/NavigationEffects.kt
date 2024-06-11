package dev.snvdr.heartrate.features.navigation.utils

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import dev.snvdr.heartrate.features.navigation.navigator.NavigationAction
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun NavigationEffects(
    navigationChannel:Channel<NavigationAction>,
    navHostController: NavHostController
){
    val activity = (LocalContext.current as? Activity)
    LaunchedEffect(activity, navHostController, navigationChannel){
        navigationChannel.receiveAsFlow().collect{ action ->
            if (activity?.isFinishing == true) {
                return@collect
            }
            when(action){
                is NavigationAction.NavigateBack -> {
                    if (action.route != null) {
                        navHostController.navigateBack(action.route, action.inclusive)
                    } else {
                        navHostController.navigateBack()
                    }
                }

                is NavigationAction.NavigateTo -> {
                    navHostController.navigate(action.route){
                        launchSingleTop = action.isSingleTop
                        action.popUpToRoute?.let { popUpToRoute ->
                            popUpTo(popUpToRoute){
                                inclusive = action.inclusive
                            }
                        }
                    }

                }
            }
        }
    }

}