package dev.snvdr.heartrate.models

import dev.snvdr.data.models.HeartRateDTO

fun HeartRateDTO.toHeartRate(): HeartRate {
    return HeartRate(bpm, creationTime, creationDate)
}