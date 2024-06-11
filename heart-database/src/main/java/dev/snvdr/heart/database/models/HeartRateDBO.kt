package dev.snvdr.heart.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("heartRates")
data class HeartRateDBO(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    val bpm:Int,
    val creationTime:Long,
    val creationDate:Long
)
