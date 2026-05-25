package com.edu.pdf.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.edu.pdf.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PdfNotificationHelper @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        private const val CHANNEL_ID = "pdf_file_updates"
        private const val SILENT_CHANNEL_ID = "pdf_bg_ops"
    }

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "PDF File Updates",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifies when a PDF is added or changed"
        }
        
        val silentChannel = NotificationChannel(
            SILENT_CHANNEL_ID,
            "Background Operations",
            NotificationManager.IMPORTANCE_MIN // 🌟 SILENT: No sound, no icon popping
        ).apply {
            description = "System requirement for background tasks"
            setShowBadge(false)
        }
        
        notificationManager.createNotificationChannel(channel)
        notificationManager.createNotificationChannel(silentChannel)
    }

    fun showNewPdfNotification(fileName: String, filePath: String) {
        val now = System.currentTimeMillis()
        val lastNotified = notifiedFiles[filePath] ?: 0L
        if (now - lastNotified < 60000) return
        notifiedFiles[filePath] = now

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("pdf_to_open", filePath)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            filePath.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_notify_chat)
            .setContentTitle("New PDF Added")
            .setContentText(fileName)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(filePath.hashCode(), notification)
    }

    // 🌟 THE SILENT GUARDIAN: Required for Android 14+ background safety
    fun getMoveProgressNotification(current: Int, total: Int, message: String): android.app.Notification {
        return NotificationCompat.Builder(context, SILENT_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle("Syncing Files Safely")
            .setContentText("Operation in progress...")
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setOngoing(true)
            .setSilent(true)
            .build()
    }

    private val notifiedFiles = java.util.concurrent.ConcurrentHashMap<String, Long>()
}
