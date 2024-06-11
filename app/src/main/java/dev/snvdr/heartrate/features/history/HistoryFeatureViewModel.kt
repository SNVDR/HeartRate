package dev.snvdr.heartrate.features.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.snvdr.data.HeartRateRepository
import dev.snvdr.heartrate.features.navigation.navigator.AppNavigator
import dev.snvdr.heartrate.models.toHeartRate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryFeatureViewModel @Inject constructor(
    private val heartRateRepository: HeartRateRepository,
    private val appNavigator: AppNavigator
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryFeatureUIState())

    val state get() = _state.asStateFlow()

    init {
        observeAllHeartRates()
    }

    private fun observeAllHeartRates() {
        heartRateRepository.observeAllHeartRates().onEach { heartRates ->
            _state.update {
                it.copy(heartRates = heartRates.map { dtos -> dtos.toHeartRate() })
            }
        }.launchIn(viewModelScope)
    }

    fun clearAllHeartRates() = viewModelScope.launch{
        heartRateRepository.clearHearRates()
    }

    fun onNavigateBack(){
        appNavigator.tryNavigateBack()
    }
}