package dev.snvdr.heartrate.features.history

import dev.snvdr.heartrate.models.HeartRate

data class HistoryFeatureUIState(
    val heartRates:List<HeartRate> = emptyList()
)