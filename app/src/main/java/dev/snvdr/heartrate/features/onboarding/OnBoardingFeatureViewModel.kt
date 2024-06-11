package dev.snvdr.heartrate.features.onboarding

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.snvdr.heartrate.features.navigation.navigator.AppNavigator
import dev.snvdr.heartrate.features.navigation.utils.Destination
import javax.inject.Inject

@HiltViewModel
class OnBoardingFeatureViewModel @Inject constructor(
    private val appNavigator: AppNavigator
) : ViewModel() {

    fun onNavigateToHomePage(){
        appNavigator.tryNavigateTo(
            route = Destination.HomePage1,
            popUpToRoute = Destination.OnBoarding,
            inclusive = true
        )
    }

}