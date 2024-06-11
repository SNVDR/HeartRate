package dev.snvdr.heartrate.features.navigation.utils

import kotlinx.serialization.Serializable

sealed interface Destination{
    @Serializable
    data object Loading: Destination
    @Serializable
    data object OnBoarding: Destination
    @Serializable
    data object HomePage1: Destination
    @Serializable
    data object HomePage2: Destination
    @Serializable
    data class Result(val id:Long): Destination
    @Serializable
    data object History: Destination
}