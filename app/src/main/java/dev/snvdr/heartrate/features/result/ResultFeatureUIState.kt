package dev.snvdr.heartrate.features.result

import dev.snvdr.heartrate.models.HeartRate

data class ResultFeatureUIState(
    val heartRate:HeartRate? = null
)