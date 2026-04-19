package com.burakgurgil.burak2.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.burakgurgil.burak2.MainActivity
import com.burakgurgil.burak2.R

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val noteId = intent.getLongExtra("NOTE_ID", -1L)
        val noteTitle = intent.getStringExtra("NOTE_TITLE") ?: "Hatırlatıcı"
        val noteContent = intent.getStringExtra("NOTE_CONTENT") ?: "Bir notunuz için hatırlatıcı."

        showNotification(context, noteTitle, noteContent, noteId.toInt())
    }

    private fun showNotification(context: Context, title: String, content: String, notificationId: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "note_reminders_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Not Hatırlatıcıları",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notlar için ayarlanan hatırlatıcı bildirimleri"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            // Maybe add extra to open the specific note
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Using android default icon if R.drawable.ic_launcher is missing, we will see. Usually we should use a custom icon.
        // I will use a built-in standard icon from android for safety if app doesn't have an icon yet, but usually it's R.mipmap.ic_launcher
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, builder.build())
    }

    companion object {
        fun scheduleReminder(context: Context, noteId: Long, title: String, content: String, timeInMillis: Long) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
            val intent = Intent(context, ReminderReceiver::class.java).apply {
                putExtra("NOTE_ID", noteId)
                putExtra("NOTE_TITLE", title)
                putExtra("NOTE_CONTENT", content)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                noteId.toInt(),
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(android.app.AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
                } else {
                    alarmManager.setExact(android.app.AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
                }
            } catch (e: SecurityException) {
                // Handle SecurityException if SCHEDULE_EXACT_ALARM is denied
                e.printStackTrace()
            }
        }

        fun cancelReminder(context: Context, noteId: Long) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
            val intent = Intent(context, ReminderReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                noteId.toInt(),
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            alarmManager.cancel(pendingIntent)
        }
    }
}
