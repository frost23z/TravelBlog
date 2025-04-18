package me.zayedbinhasan.travelblog.ui.screen.list

import me.zayedbinhasan.travelblog.mvi.Intent

sealed class ListIntent : Intent {
    data object Refresh : ListIntent()
    data class BlogClicked(val blogId: Int) : ListIntent()
    data object LoadBlogs : ListIntent()
}