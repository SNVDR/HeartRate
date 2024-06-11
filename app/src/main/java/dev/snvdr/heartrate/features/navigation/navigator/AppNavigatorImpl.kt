package dev.snvdr.heartrate.features.navigation.navigator

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

class AppNavigatorImpl @Inject constructor() : AppNavigator {
    override val navigationChannel = Channel<NavigationAction>(
        capacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )

    override suspend fun navigateBack(route: Any?, inclusive: Boolean) {
       navigationChannel.send(NavigationAction.NavigateBack(route,inclusive))
    }

    override fun tryNavigateBack(route: Any?, inclusive: Boolean) {
        navigationChannel.trySend(
            NavigationAction.NavigateBack(
                route = route,
                inclusive = inclusive
            )
        )
    }

    override suspend fun navigateTo(
        route: Any,
        popUpToRoute: Any?,
        inclusive: Boolean,
        isSingleTop: Boolean,
    ) {
        navigationChannel.send(
            NavigationAction.NavigateTo(
                route = route,
                popUpToRoute = popUpToRoute,
                inclusive = inclusive,
                isSingleTop = isSingleTop,
            )
        )
    }

    override fun tryNavigateTo(
        route: Any,
        popUpToRoute: Any?,
        inclusive: Boolean,
        isSingleTop: Boolean,
    ) {
        navigationChannel.trySend(
            NavigationAction.NavigateTo(
                route = route,
                popUpToRoute = popUpToRoute,
                inclusive = inclusive,
                isSingleTop = isSingleTop,
            )
        )
    }
}