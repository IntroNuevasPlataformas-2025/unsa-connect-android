package com.unsa.unsaconnect.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.impl.WorkManagerImpl
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

    val reminderTime: StateFlow<Pair<Int, Int>> = settingsManager.reminderTime
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 8 to 0
        )

    // API Publica para UI
    fun onReminderToggleChanged(isEnabled: Boolean) {
        viewModelScope.launch {
            settingsManager.setReminderEnabled(isEnabled)
            if(isEnabled) {
                // Programar con la hora actual
                scheduleReminder()
            } else {
                // Cancelar recordatorio
                cancelReminder()
            }
        }
    }

    fun onTimeSelected(hour: Int, minute: Int) {
        viewModelScope.launch {
            settingsManager.setReminderTime(hour, minute)

            val isActive = isReminderEnable.value
            Log.d("SettingsViewModel","Hora cambiada a ${hour}:${minute}, notificaciones activas?: $isActive")

            // Reprogramar el recordatorio con la nueva hora
            if(isReminderEnable.value) {
                scheduleReminder()
            }
        }
    }

    // Logica Gestion del worker
    // Implementacion para manejar tareas programadas de recordatorio

    private fun scheduleReminder() {
        val workManager = WorkManager.getInstance(context)
        val (hour, minute) = reminderTime.value
        val initialDelay = calculateInitialDelay(hour, minute)

        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .addTag(REMINDER_WORK_TAG)
            .build()

        workManager.enqueueUniquePeriodicWork(
            REMINDER_WORK_NAME,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, // Actualiza el trabajo existente
            workRequest
        )
    }

    private fun cancelReminder() {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelUniqueWork(REMINDER_WORK_NAME)
    }

    /**
     * Calcula el retraso inicial hasta la próxima hora y minuto especificados.
     */
    private fun calculateInitialDelay(hour: Int, minute: Int): Long {
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

        return target.timeInMillis - now.timeInMillis
    }

    // --- Constantes ---
    // Nombres y tags para WorkManager
    companion object {
        private const val REMINDER_WORK_NAME = "daily_reminder_work"
        private const val REMINDER_WORK_TAG = "reminder_tag"
    }
}