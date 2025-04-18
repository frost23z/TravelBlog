package me.zayedbinhasan.travelblog.ui.screen.list

import me.zayedbinhasan.travelblog.domain.model.Blog
import me.zayedbinhasan.travelblog.mvi.State
import me.zayedbinhasan.travelblog.util.NetworkError

data class ListState(
    val blogs: List<Blog> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: NetworkError? = null
) : State