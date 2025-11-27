package com.unsa.unsaconnect.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unsa.unsaconnect.data.local.SettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsManager: SettingsManager
) : ViewModel() {

    val isReminderEnable : StateFlow<Boolean> = settingsManager.isReminderEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun toggleReminder(isEnabled: Boolean) {
        viewModelScope.launch {
            settingsManager.setReminderEnabled(isEnabled)
        }
    }

    suspend fun setReminderEnabled(enabled: Boolean) {
        settingsManager.setReminderEnabled(enabled)
    }
}