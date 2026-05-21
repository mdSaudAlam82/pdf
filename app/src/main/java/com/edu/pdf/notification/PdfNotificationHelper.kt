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
        // 🌟 ELITE FIX 1: Cache to prevent spam/duplicate bursts
        private val notifiedFiles = java.util.concurrent.ConcurrentHashMap<String, Long>()
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "PDF File Updates",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifies when a PDF is added or changed"
            enableVibration(true)
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun showNewPdfNotification(fileName: String, filePath: String) {
        val now = System.currentTimeMillis()

        // 🌟 ELITE FIX 1: Agar pichle 60 seconds me is file ka notification bheja hai, toh Cancel karo!
        val lastNotified = notifiedFiles[filePath] ?: 0L
        if (now - lastNotified < 60000) {
            return
        }
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
}