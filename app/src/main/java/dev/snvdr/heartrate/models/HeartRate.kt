package dev.snvdr.heartrate.models

import kotlinx.serialization.Serializable

@Serializable
data class HeartRate(
    val bpm: Int,
    val creationTime: Long,
    val creationDate: Long,
)