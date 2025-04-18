package me.zayedbinhasan.travelblog.ui.screen.detail


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import me.zayedbinhasan.travelblog.domain.repository.Repository
import me.zayedbinhasan.travelblog.mvi.BaseViewModel
import me.zayedbinhasan.travelblog.navigation.Destination
import me.zayedbinhasan.travelblog.navigation.Navigator
import me.zayedbinhasan.travelblog.util.NetworkError
import me.zayedbinhasan.travelblog.util.onError
import me.zayedbinhasan.travelblog.util.onSuccess

class DetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val navigator: Navigator,
    private val repository: Repository
) : BaseViewModel<DetailIntent, DetailState, DetailEffect>(DetailState()) {

    init {
        handleLoadBlog()
    }

    override fun handleIntent(intent: DetailIntent) {
        when (intent) {
            is DetailIntent.NavigateBack -> handleBackClicked()
        }
    }

    private fun handleBackClicked() {
        viewModelScope.launch {
            navigator.navigateUp()
        }
    }

    private fun handleLoadBlog() {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }
            try {
                val detail = savedStateHandle.toRoute<Destination.DetailDestination>()
                val blogId = detail.id
                repository.getBlogsFromLocalById(blogId).onSuccess { blogFlow ->
                    blogFlow.collect { blog ->
                        updateState {
                            it.copy(
                                blog = blog,
                                isLoading = false
                            )
                        }
                    }
                }.onError {
                    updateState {
                        it.copy(
                            errorMessage = NetworkError.UNKNOWN,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                updateState {
                    it.copy(
                        errorMessage = NetworkError.UNKNOWN,
                        isLoading = false
                    )
                }
            }
        }
    }
}