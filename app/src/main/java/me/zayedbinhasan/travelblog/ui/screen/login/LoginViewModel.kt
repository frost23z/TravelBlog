package me.zayedbinhasan.travelblog.ui.screen.login

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.zayedbinhasan.travelblog.mvi.BaseViewModel
import me.zayedbinhasan.travelblog.navigation.Destination
import me.zayedbinhasan.travelblog.navigation.Navigator
import me.zayedbinhasan.travelblog.preference.PreferencesManager

class LoginViewModel(
    private val navigator: Navigator,
    private val preferencesManager: PreferencesManager
) : BaseViewModel<LoginIntent, LoginState, LoginEffect>(LoginState()) {

    init {
        viewModelScope.launch {
            preferencesManager.isLoggedInFlow.collect { isLoggedIn ->
                if (isLoggedIn) {
                    navigator.navigate(Destination.ListDestination) {
                        popUpTo(Destination.AuthDestination) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    override fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.UsernameChanged -> handleUsernameChanged(intent.username)
            is LoginIntent.PasswordChanged -> handlePasswordChanged(intent.password)
            is LoginIntent.LoginClicked -> handleLoginClicked()
            is LoginIntent.DismissDialog -> handleDismissDialog()
        }
    }

    private fun handleUsernameChanged(username: String) {
        updateState {
            it.copy(
                username = username,
                usernameError = validateUsername(username)
            )
        }
    }

    private fun handlePasswordChanged(password: String) {
        updateState {
            it.copy(
                password = password,
                passwordError = validatePassword(password)
            )
        }
    }

    private fun handleLoginClicked() {
        if (validateCredentials()) {
            viewModelScope.launch {
                updateState { it.copy(showDialog = true) }
            }
        }
    }

    private fun handleDismissDialog() {
        updateState { it.copy(showDialog = false) }
        viewModelScope.launch {
            navigator.navigate(Destination.ListDestination) {
                popUpTo(Destination.AuthDestination) {
                    inclusive = true
                }
            }
            preferencesManager.setLoggedIn(true)
        }
    }

    private fun validateUsername(username: String): String? {
        return when {
            username.isEmpty() -> "Please enter a username"
            username.length < 3 -> "Username must be at least 3 characters long"
            else -> null
        }
    }

    private fun validatePassword(password: String): String? {
        return when {
            password.isEmpty() -> "Please enter a password"
            password.length < 6 -> "Password must be at least 6 characters long"
            else -> null
        }
    }

    private fun validateCredentials(): Boolean {
        val currentState = state.value
        val usernameError = validateUsername(currentState.username)
        val passwordError = validatePassword(currentState.password)

        updateState {
            it.copy(
                usernameError = usernameError,
                passwordError = passwordError
            )
        }

        return usernameError == null && passwordError == null
    }
}