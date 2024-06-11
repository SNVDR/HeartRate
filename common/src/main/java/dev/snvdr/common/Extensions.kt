package dev.snvdr.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toTime(): String {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val creationTimeDate = Date(this)
    val creationTimeString = timeFormat.format(creationTimeDate)
    return creationTimeString
}

fun Long.toDate(): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val creationDateDate = Date(this)
    val creationDateString = dateFormat.format(creationDateDate)

    return creationDateString
}

