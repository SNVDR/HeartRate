package dev.snvdr.heartrate.features.homepage.hp_2

data class HomePageUIState(
    val isCameraCovered:Boolean = false,
    val progressOfMeasurement:Float = 0F,
    val isMeasurementCompleted:Long? = null,
    val bpm:Int = 0
)
