package me.zayedbinhasan.travelblog.mvi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Base ViewModel interface for MVI architecture
 * Defines the contract for handling intents, states, and effects
 */
interface ViewModelContract<I : Intent, S : State, E : Effect> {
    /**
     * Current UI state as a StateFlow
     */
    val state: StateFlow<S>

    /**
     * Stream of side effects
     */
    val effect: Flow<E>

    /**
     * Process user intents
     */
    fun processIntent(intent: I)
}