package com.unsa.unsaconnect.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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
        val REMINDER_HOUR_KEY = intPreferencesKey("reminder_hour")
        val REMINDER_MINUTE_KEY = intPreferencesKey("reminder_minute")
    }

    // Leer la preferencia de recordatorio
    val isReminderEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[REMINDER_ENABLED_KEY] ?: false
        }

    val reminderTime: Flow<Pair<Int, Int>> = context.dataStore.data
        .map { preferences ->
            val hour = preferences[REMINDER_HOUR_KEY] ?: 8 // Default a las 8 AM
            val minute = preferences[REMINDER_MINUTE_KEY] ?: 0
            Pair(hour, minute)
        }

    suspend fun setReminderTime(hour: Int, minute: Int) {
        context.dataStore.edit { preferences ->
            preferences[REMINDER_HOUR_KEY] = hour
            preferences[REMINDER_MINUTE_KEY] = minute
        }
    }

    suspend fun setReminderEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[REMINDER_ENABLED_KEY] = enabled
        }
    }
}