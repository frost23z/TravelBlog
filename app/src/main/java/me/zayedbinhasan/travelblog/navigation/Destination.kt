package me.zayedbinhasan.travelblog.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Destination {
    @Serializable
    data object AuthDestination : Destination()

    @Serializable
    data object LoginDestination : Destination()

    @Serializable
    data object MainDestination : Destination()

    @Serializable
    data object ListDestination : Destination()

    @Serializable
    data class DetailDestination(val id: Int) : Destination()
}