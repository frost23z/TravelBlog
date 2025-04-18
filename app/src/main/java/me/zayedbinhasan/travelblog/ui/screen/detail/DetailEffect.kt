package me.zayedbinhasan.travelblog.ui.screen.detail

import me.zayedbinhasan.travelblog.mvi.Effect

sealed class DetailEffect : Effect {
    data object NavigateBack : DetailEffect()
}