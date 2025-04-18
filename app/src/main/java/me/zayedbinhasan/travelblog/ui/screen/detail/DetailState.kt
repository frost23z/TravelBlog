package me.zayedbinhasan.travelblog.ui.screen.detail

import me.zayedbinhasan.travelblog.domain.model.Blog
import me.zayedbinhasan.travelblog.mvi.State
import me.zayedbinhasan.travelblog.util.NetworkError

data class DetailState(
    val blog: Blog? = null,
    val isLoading: Boolean = false,
    val errorMessage: NetworkError? = null
) : State