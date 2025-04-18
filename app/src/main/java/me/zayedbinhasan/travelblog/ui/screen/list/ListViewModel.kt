package me.zayedbinhasan.travelblog.ui.screen.list

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.zayedbinhasan.travelblog.domain.repository.Repository
import me.zayedbinhasan.travelblog.mvi.BaseViewModel
import me.zayedbinhasan.travelblog.navigation.Destination
import me.zayedbinhasan.travelblog.navigation.Navigator
import me.zayedbinhasan.travelblog.util.NetworkError
import me.zayedbinhasan.travelblog.util.onError
import me.zayedbinhasan.travelblog.util.onSuccess

class ListViewModel(
    private val navigator: Navigator,
    private val repository: Repository
) : BaseViewModel<ListIntent, ListState, ListEffect>(ListState()) {

    init {
        processIntent(ListIntent.LoadBlogs)
    }

    private fun handleLoadBlogs() {
        updateState { it.copy(isLoading = true) }

        viewModelScope.launch {
            refreshBlogsFromNetwork()
            repository.getBlogsFromLocal().onSuccess { blogListFlow ->
                blogListFlow.collectLatest { blogList ->
                    updateState { it.copy(blogs = blogList) }
                }
            }.onError {
                updateState { it.copy(errorMessage = NetworkError.UNKNOWN) }
            }
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
        // sendEffect(ListEffect.NavigateToDetail(blogId))
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
        }
    }
}