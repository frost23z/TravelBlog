package me.zayedbinhasan.travelblog.navigation

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * Default implementation of the Navigator
 */
class DefaultNavigator(override val startDestination: Destination) : Navigator {

    private val _navigationActions = Channel<NavigationActions>(Channel.BUFFERED)
    override val navigationActions = _navigationActions.receiveAsFlow()

    override suspend fun navigate(
        destination: Destination,
        navOptions: NavOptionsBuilder.() -> Unit
    ) {
        _navigationActions.send(NavigationActions.Navigate(destination, navOptions))
    }

    override suspend fun navigateUp() {
        _navigationActions.send(NavigationActions.NavigateUp)
    }
}