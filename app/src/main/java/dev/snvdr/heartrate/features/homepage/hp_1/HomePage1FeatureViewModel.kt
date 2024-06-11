package dev.snvdr.heartrate.features.homepage.hp_1

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.snvdr.heartrate.features.navigation.navigator.AppNavigator
import dev.snvdr.heartrate.features.navigation.utils.Destination
import javax.inject.Inject

@HiltViewModel
class HomePage1FeatureViewModel @Inject constructor(
    private val appNavigator: AppNavigator
): ViewModel(){

    fun onNavigateToHomePage2(){
        appNavigator.tryNavigateTo(Destination.HomePage2)
    }

    fun onNavigateToHistory(){
        appNavigator.tryNavigateTo(Destination.History)
    }

}