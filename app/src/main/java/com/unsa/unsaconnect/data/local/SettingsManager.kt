package com.unsa.unsaconnect.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsManager @Inject constructor(
    private val context: Context
){
    companion object {
        val REMINDER_ENABLED_KEY = booleanPreferencesKey("reminder_enabled")
    }

    // Leer la preferencia de recordatorio
    val isReminderEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[REMINDER_ENABLED_KEY] ?: false
        }

    suspend fun setReminderEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[REMINDER_ENABLED_KEY] = enabled
        }
    }
}