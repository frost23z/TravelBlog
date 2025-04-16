package me.zayedbinhasan.travelblog.ui.screen.login

import me.zayedbinhasan.travelblog.mvi.Intent

sealed class LoginIntent : Intent {
    data class UsernameChanged(val username: String) : LoginIntent()
    data class PasswordChanged(val password: String) : LoginIntent()
    data object LoginClicked : LoginIntent()
    data object DismissDialog : LoginIntent()
}