package me.zayedbinhasan.travelblog.ui.screen.list

import me.zayedbinhasan.travelblog.mvi.Intent

sealed class ListIntent : Intent {
    data object Refresh : ListIntent()
    data class BlogClicked(val blogId: Int) : ListIntent()
    data object LoadBlogs : ListIntent()
    data object ToggleSortDialog : ListIntent()
    data class SortBlogs(val sort: Sort) : ListIntent()
    data object SwitchSortOrder : ListIntent()
    data class Search(val query: String) : ListIntent()
    data object ToggleSearchBarVisibility : ListIntent()
}