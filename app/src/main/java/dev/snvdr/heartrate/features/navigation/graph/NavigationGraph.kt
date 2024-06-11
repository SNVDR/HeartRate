package dev.snvdr.heartrate.features.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.snvdr.heartrate.features.history.HistoryFeatureUI
import dev.snvdr.heartrate.features.homepage.hp_1.HomePage1FeatureUI
import dev.snvdr.heartrate.features.homepage.hp_2.HomePage2FeatureUI
import dev.snvdr.heartrate.features.loading.LoadingUIFeatureUI
import dev.snvdr.heartrate.features.navigation.utils.Destination
import dev.snvdr.heartrate.features.onboarding.OnBoardingFeatureUI
import dev.snvdr.heartrate.features.result.ResultFeatureUI

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = Destination.Loading
    ) {
        composable<Destination.Loading> {
            LoadingUIFeatureUI()
        }

        composable<Destination.OnBoarding> {
            OnBoardingFeatureUI()
        }

        composable<Destination.HomePage1> {
            HomePage1FeatureUI()
        }

        composable<Destination.HomePage2> {
            HomePage2FeatureUI()
        }

        composable<Destination.Result> { backStackEntry ->
            val id = backStackEntry.toRoute<Destination.Result>().id
            ResultFeatureUI(id)
        }

        composable<Destination.History> {
            HistoryFeatureUI()
        }
    }
}