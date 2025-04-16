package me.zayedbinhasan.travelblog.ui.screen.login

import me.zayedbinhasan.travelblog.mvi.Effect


sealed class LoginEffect : Effect {
    data object NavigateToList : Effect
}