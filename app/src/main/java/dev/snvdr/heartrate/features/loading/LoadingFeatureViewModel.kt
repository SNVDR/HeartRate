package dev.snvdr.heartrate.features.loading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.snvdr.heartrate.features.navigation.navigator.AppNavigator
import dev.snvdr.heartrate.features.navigation.utils.Destination
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoadingFeatureViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
) : ViewModel() {
    private val _state = MutableStateFlow(LoadingFeatureUIState())
    val state get() = _state.asStateFlow()

    init {
        startLoading()
    }

    private fun startLoading() = viewModelScope.launch {
        while (_state.value.progress < PROGRESS_UPPER_BOUND) {
            delay(INCREMENT_DELAY)
            incrementProgress()
        }
    }

    private fun incrementProgress() {
        _state.update { currentState ->
            val newProgress = currentState.progress + PROGRESS_STEP
            currentState.copy(
                progress = newProgress,
                isProgressFinished = newProgress >= PROGRESS_UPPER_BOUND
            )
        }
        if (_state.value.progress >= PROGRESS_UPPER_BOUND) {
            onNavigateToOnBoarding()
        }
    }

    private fun onNavigateToOnBoarding() {
        appNavigator.tryNavigateTo(
            route = Destination.OnBoarding,
            popUpToRoute = Destination.Loading,
            inclusive = true
        )
    }

    private companion object {
        const val PROGRESS_STEP = 0.01f
        const val PROGRESS_UPPER_BOUND = 1f
        const val INCREMENT_DELAY = 40L
    }
}