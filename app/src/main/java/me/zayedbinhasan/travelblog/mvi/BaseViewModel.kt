package me.zayedbinhasan.travelblog.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Base implementation of MviViewModel that handles the common functionality
 */
abstract class BaseViewModel<I : Intent, S : State, E : Effect>(
    initialState: S
) : ViewModel(), ViewModelContract<I, S, E> {

    private val _state = MutableStateFlow(initialState)
    override val state: StateFlow<S> = _state.asStateFlow()

    private val _effects = Channel<E>()
    override val effect = _effects.receiveAsFlow()

    override fun processIntent(intent: I) {
        handleIntent(intent)
    }

    /**
     * Handle the given intent and produce new state or effects
     */
    protected abstract fun handleIntent(intent: I)

    /**
     * Update the current state
     */
    protected fun updateState(reducer: (S) -> S) {
        val newState = reducer(_state.value)
        _state.value = newState
    }

    /**
     * Send a one-time side effect
     */
    protected fun sendEffect(effect: E) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }
}