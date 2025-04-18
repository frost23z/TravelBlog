package me.zayedbinhasan.travelblog.ui.screen.detail

import me.zayedbinhasan.travelblog.mvi.Intent

sealed class DetailIntent : Intent {
    data object NavigateBack : DetailIntent()
}