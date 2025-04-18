package me.zayedbinhasan.travelblog.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.zayedbinhasan.travelblog.ui.screen.list.Sort
import me.zayedbinhasan.travelblog.ui.screen.list.SortOrder

class PreferencesManager(private val dataStore: DataStore<Preferences>) {
    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    private val SORT_BY = intPreferencesKey("sort_by")
    private val SORT_ORDER = intPreferencesKey("sort_order")

    val isLoggedInFlow: Flow<Boolean> = dataStore.data
        .map { it[IS_LOGGED_IN] ?: false }

    suspend fun setLoggedIn(value: Boolean) {
        dataStore.edit { it[IS_LOGGED_IN] = value }
    }

    val sortFlow: Flow<Sort> = dataStore.data.map {
        Sort.entries.getOrElse(index = it[SORT_BY] ?: Sort.DEFAULT.ordinal) {
            Sort.DEFAULT
        }
    }

    suspend fun setSort(value: Sort) {
        dataStore.edit { it[SORT_BY] = value.ordinal }
    }

    val sortOrderFlow: Flow<SortOrder> = dataStore.data.map {
        SortOrder.entries.getOrElse(index = it[SORT_ORDER] ?: SortOrder.ASCENDING.ordinal) {
            SortOrder.ASCENDING
        }
    }

    suspend fun setSortOrder(value: SortOrder) {
        dataStore.edit { it[SORT_ORDER] = value.ordinal }
    }
}