package me.zayedbinhasan.travelblog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import me.zayedbinhasan.travelblog.navigation.Destination
import me.zayedbinhasan.travelblog.navigation.NavigationActions
import me.zayedbinhasan.travelblog.navigation.Navigator
import me.zayedbinhasan.travelblog.ui.screen.detail.DetailScreen
import me.zayedbinhasan.travelblog.ui.screen.detail.DetailViewModel
import me.zayedbinhasan.travelblog.ui.screen.list.ListScreen
import me.zayedbinhasan.travelblog.ui.screen.list.ListViewModel
import me.zayedbinhasan.travelblog.ui.screen.login.LoginScreen
import me.zayedbinhasan.travelblog.ui.screen.login.LoginViewModel
import me.zayedbinhasan.travelblog.ui.theme.TravelBlogTheme
import me.zayedbinhasan.travelblog.util.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TravelBlogTheme {
                val navController = rememberNavController()
                val navigator = koinInject<Navigator>()

                ObserveAsEvents(flow = navigator.navigationActions) { action ->
                    when (action) {
                        is NavigationActions.Navigate -> {
                            navController.navigate(action.destination) {
                                action.navOptions(this)
                            }
                        }

                        NavigationActions.NavigateUp -> navController.navigateUp()
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = koinInject<Navigator>().startDestination
                ) {
                    navigation<Destination.AuthDestination>(
                        startDestination = Destination.LoginDestination,
                    ) {
                        composable<Destination.LoginDestination> {
                            val viewModel = koinViewModel<LoginViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()
                            LoginScreen(state = state, onIntent = viewModel::processIntent)
                        }
                    }
                    navigation<Destination.MainDestination>(
                        startDestination = Destination.ListDestination,
                    ) {
                        composable<Destination.ListDestination> {
                            val viewModel = koinViewModel<ListViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()
                            ListScreen(state = state, onIntent = viewModel::processIntent)
                        }
                        composable<Destination.DetailDestination> {
                            val viewModel = koinViewModel<DetailViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()
                            DetailScreen(state = state, onIntent = viewModel::processIntent)
                        }
                    }
                }
            }
        }
    }
}