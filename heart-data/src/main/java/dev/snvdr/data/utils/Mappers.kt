package dev.snvdr.data.utils

import dev.snvdr.data.models.HeartRateDTO
import dev.snvdr.heart.database.models.HeartRateDBO

fun HeartRateDBO.toHeartRateDTO(): HeartRateDTO {
    return HeartRateDTO(bpm, creationTime, creationDate)
}

fun HeartRateDTO.toHeartRateDBO(): HeartRateDBO {
    return HeartRateDBO(bpm = bpm, creationTime = creationTime, creationDate = creationDate)
}