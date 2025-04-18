package me.zayedbinhasan.travelblog.ui.screen.list

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.zayedbinhasan.travelblog.domain.repository.Repository
import me.zayedbinhasan.travelblog.mvi.BaseViewModel
import me.zayedbinhasan.travelblog.navigation.Destination
import me.zayedbinhasan.travelblog.navigation.Navigator
import me.zayedbinhasan.travelblog.preference.PreferencesManager
import me.zayedbinhasan.travelblog.util.NetworkError
import me.zayedbinhasan.travelblog.util.onError
import me.zayedbinhasan.travelblog.util.onSuccess

class ListViewModel(
    private val navigator: Navigator,
    private val repository: Repository,
    private val preferencesManager: PreferencesManager
) : BaseViewModel<ListIntent, ListState, ListEffect>(ListState()) {

    init {
        viewModelScope.launch {
            preferencesManager.sortFlow.collectLatest { sort ->
                updateState { it.copy(sort = sort) }
                handleDatabaseCall()
            }
        }
        viewModelScope.launch {
            preferencesManager.sortOrderFlow.collectLatest { sortOrder ->
                updateState { it.copy(sortOrder = sortOrder) }
                handleDatabaseCall()
            }
        }
        processIntent(ListIntent.LoadBlogs)
    }

    private fun handleLoadBlogs() {
        updateState { it.copy(isLoading = true) }

        viewModelScope.launch {
            refreshBlogsFromNetwork()
            handleDatabaseCall()
        }
    }

    private fun changeSort(sort: Sort, sortOrder: SortOrder = SortOrder.ASCENDING) {
        viewModelScope.launch {
            preferencesManager.setSort(sort)
            preferencesManager.setSortOrder(sortOrder)
        }
    }

    private suspend fun handleDatabaseCall() {
        repository.getBlogsFromLocal(state.value.sort, state.value.sortOrder)
            .onSuccess { blogListFlow ->
                blogListFlow.collectLatest { blogList ->
                    updateState { it.copy(blogs = blogList) }
                }
            }.onError {
            updateState { it.copy(errorMessage = NetworkError.UNKNOWN) }
        }
    }

    private fun handleRefresh() {
        viewModelScope.launch {
            updateState { it.copy(isRefreshing = true) }
            refreshBlogsFromNetwork()
            updateState { it.copy(isRefreshing = false) }
        }
    }

    private fun handleBlogClicked(blogId: Int) {
        viewModelScope.launch {
            navigator.navigate(Destination.DetailDestination(blogId)) {
                popUpTo(Destination.ListDestination) {
                    inclusive = false
                }
            }
        }
    }

    private val toggleSortDialog: () -> Unit = {
        updateState { it.copy(showSortDialog = !state.value.showSortDialog) }
    }

    private suspend fun refreshBlogsFromNetwork() {
        repository.getBlogsFromRemoteToLocal().onSuccess {
            updateState { it.copy(isLoading = false) }
        }.onError { error ->
            updateState { it.copy(errorMessage = error) }
        }
    }

    override fun handleIntent(intent: ListIntent) {
        when (intent) {
            is ListIntent.BlogClicked -> handleBlogClicked(intent.blogId)
            ListIntent.LoadBlogs -> handleLoadBlogs()
            ListIntent.Refresh -> handleRefresh()
            is ListIntent.SortBlogs -> changeSort(intent.sort)
            ListIntent.SwitchSortOrder -> changeSort(
                state.value.sort,
                if (state.value.sortOrder == SortOrder.ASCENDING) SortOrder.DESCENDING else SortOrder.ASCENDING
            )

            ListIntent.ToggleSortDialog -> toggleSortDialog()
        }
    }
}