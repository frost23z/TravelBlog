package me.zayedbinhasan.travelblog.ui.screen.login

import me.zayedbinhasan.travelblog.mvi.State

data class LoginState(
    val isLoading: Boolean = false,
    val username: String = "",
    val password: String = "",
    val usernameError: String? = null,
    val passwordError: String? = null,
    val enabled: Boolean = true,
    val showDialog: Boolean = false
) : State