package com.unsa.unsaconnect.data.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.unsa.unsaconnect.data.local.SettingsManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val settingsManager: SettingsManager
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        // Verificar si las notificaciones están habilitadas en la configuración
        val isEnabled = settingsManager.isReminderEnabled.first()
        if (!isEnabled) {
            return Result.success()
        }

        showNotification()

        return Result.success()
    }

    private fun showNotification() {
        val channelId = "daily_reminder_channel"
        val notificationId = 1001
        val context = applicationContext

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crear canal para Android O y superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Recordatorios Diarios",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifiaciones diarias de UNSA Connect"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(com.unsa.unsaconnect.R.drawable.outline_notifications_24) // Asegúrate de tener este ícono en tu carpeta drawable
            .setContentTitle("!No te pierdas nada!")
            .setContentText("Revisa las últimas novedades en UNSA Connect.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}