package com.unsa.unsaconnect.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.unsa.unsaconnect.data.local.SettingsManager
import com.unsa.unsaconnect.data.workers.ReminderWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsManager: SettingsManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val isReminderEnable : StateFlow<Boolean> = settingsManager.isReminderEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val reminderTime = settingsManager.reminderTime
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 8 to 0
        )

    fun toggleReminder(isEnabled: Boolean) {
        viewModelScope.launch {
            settingsManager.setReminderEnabled(isEnabled)
            if (isEnabled) {
                // Programar con la hora actual
                scheduleReminder(reminderTime.value.first, reminderTime.value.second)
            } else {
                // Cancelar recordatorio
                cancelReminder()
            }
        }
    }

    fun updateTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            settingsManager.setReminderTime(hour, minute)

            val isActive = isReminderEnable.value
            Log.d("SettingsViewModel","Hora cambiada a ${hour}:${minute}, notificaciones activas?: $isActive")

            // Reprogramar el recordatorio con la nueva hora
            if (isReminderEnable.value) {
                scheduleReminder(hour, minute)
            }
        }
    }

    private fun scheduleReminder(hour: Int, minute: Int) {
        val workManager = WorkManager.getInstance(context)
        val workName = "daily_reminder_work"

        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        if (target.before(now)) {
            // Si la hora objetivo ya pasó hoy, programar para mañana
            target.add(Calendar.DAY_OF_YEAR, 1)
        }

        val initialDelay = target.timeInMillis - now.timeInMillis

        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            workName,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, // Reemplaza el trabajo existente
            workRequest
        )

    }

    private fun cancelReminder() {
        val workManager = WorkManager.getInstance(context)
        val workName = "daily_reminder_work"
        workManager.cancelUniqueWork(workName)
    }

    suspend fun setReminderEnabled(enabled: Boolean) {
        settingsManager.setReminderEnabled(enabled)
    }
}