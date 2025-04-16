package me.zayedbinhasan.travelblog.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesManager(private val dataStore: DataStore<Preferences>) {
    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")

    val isLoggedInFlow: Flow<Boolean> = dataStore.data
        .map { it[IS_LOGGED_IN] ?: false }

    suspend fun setLoggedIn(value: Boolean) {
        dataStore.edit { it[IS_LOGGED_IN] = value }
    }
}