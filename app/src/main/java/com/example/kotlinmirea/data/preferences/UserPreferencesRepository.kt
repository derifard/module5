package com.example.kotlinmirea.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(private val context: Context) {

    companion object {
        val HIGHLIGHT_COMPLETED = booleanPreferencesKey("highlight_completed")
    }

    val highlightCompleted: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[HIGHLIGHT_COMPLETED] ?: false
    }

    suspend fun setHighlightCompleted(value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[HIGHLIGHT_COMPLETED] = value
        }
    }
}