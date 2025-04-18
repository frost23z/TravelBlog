package me.zayedbinhasan.travelblog.ui.screen.list

import me.zayedbinhasan.travelblog.mvi.Effect

sealed class ListEffect : Effect {
    data class NavigateToDetail(val blogId: Int) : ListEffect()
}