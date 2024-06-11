package dev.snvdr.heartrate.features.loading

data class LoadingFeatureUIState(
    val progress:Float = 0F,
    val isProgressFinished:Boolean = false
)