package me.zayedbinhasan.travelblog.navigation

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.flow.Flow

/**
 * Navigator interface for handling navigation in the app
 */
interface Navigator {
    val startDestination: Destination
    val navigationActions: Flow<NavigationActions>

    suspend fun navigate(destination: Destination, navOptions: NavOptionsBuilder.() -> Unit = {})

    suspend fun navigateUp()
}