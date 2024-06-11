package dev.snvdr.heartrate.features.homepage.hp_2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.snvdr.data.HeartRateRepository
import dev.snvdr.data.models.HeartRateDTO
import dev.snvdr.heartrate.features.navigation.navigator.AppNavigator
import dev.snvdr.heartrate.features.navigation.utils.Destination
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

@HiltViewModel
class HomePage2ViewModel @Inject constructor(
    private val repository: HeartRateRepository,
    private val appNavigator: AppNavigator,
) : ViewModel() {

    private val _state = MutableStateFlow(HomePageUIState())
    val state get() = _state.asStateFlow()

    private var measurementJob: Job? = null
    private var bpmJob: Job? = null
    fun isCameraCovered(status: Boolean) = viewModelScope.launch {
        _state.update { it.copy(isCameraCovered = status) }
        if (status) {
            startMeasurementJob()
        } else {
            stopMeasurementJob()
        }
    }

    private fun startMeasurementJob() {
        if (measurementJob?.isActive == true || bpmJob?.start() == true) return

        measurementJob = startMeasurement()
        bpmJob = generateRandomBpm()
    }

    private fun stopMeasurementJob() {
        measurementJob?.cancel()
        bpmJob?.cancel()

        measurementJob = null
        bpmJob = null

        _state.update { it.copy(bpm = 0) }
    }

    private fun startMeasurement() = viewModelScope.launch {
        var initProgress = 0f
        _state.update {
            it.copy(
                progressOfMeasurement = initProgress,
                isMeasurementCompleted = null
            )
        }
        while (initProgress < 1f) {
            delay(PROGRESS_DELAY_MS)
            initProgress += PROGRESS_INCREMENT
            _state.update { it.copy(progressOfMeasurement = initProgress) }

            if (initProgress >= 1f) {
                completeMeasurement()
                break
            }
        }
    }

    private fun generateRandomBpm() = viewModelScope.launch {
        _state.update { it.copy(bpm = 0) }
        var currentBpm = 0
        while (currentBpm <= MAX_BPM) {
            currentBpm += Random.nextInt(1..5)
            _state.update {
                it.copy(bpm = currentBpm)
            }
            delay(200L)
        }
    }

    private suspend fun completeMeasurement() {
        val idOfEntity = repository.addHeartRate(
            HeartRateDTO(
                bpm = _state.value.bpm,
                creationTime = System.currentTimeMillis(),
                creationDate = System.currentTimeMillis()
            )
        )
        _state.update { it.copy(isMeasurementCompleted = idOfEntity) }
        onNavigateToResult()
    }

    fun onNavigateBack() {
        appNavigator.tryNavigateBack(Destination.HomePage1)
    }

    private fun onNavigateToResult() {
        _state.value.isMeasurementCompleted?.let {
            appNavigator.tryNavigateTo(Destination.Result(it))
        }
    }

    override fun onCleared() {
        super.onCleared()
        measurementJob?.cancel()
        bpmJob?.cancel()

        measurementJob = null
        bpmJob = null
    }

    private companion object {
        const val PROGRESS_INCREMENT = 0.01f
        const val PROGRESS_DELAY_MS = 50L
        const val BPM_UPDATE_DELAY_MS = 50L
        const val MAX_BPM = 150
    }
}