package dev.snvdr.heartrate.features.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.snvdr.data.HeartRateRepository
import dev.snvdr.heartrate.features.navigation.navigator.AppNavigator
import dev.snvdr.heartrate.features.navigation.utils.Destination
import dev.snvdr.heartrate.models.toHeartRate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultFeatureViewModel @Inject constructor(
    private val heartRateRepository: HeartRateRepository,
    private val appNavigator: AppNavigator,
) : ViewModel() {

    private val _state = MutableStateFlow(ResultFeatureUIState())
    val state get() = _state.asStateFlow()

    fun getById(id:Long) = viewModelScope.launch{
       val heartRate = heartRateRepository.getHeartRateById(id)
        _state.update {
            it.copy(heartRate = heartRate?.toHeartRate())
        }
    }

    fun onNavigateToHistory(){
        appNavigator.tryNavigateTo(Destination.History)
    }

    fun onNavigateBack(){
        appNavigator.tryNavigateBack()
    }

}