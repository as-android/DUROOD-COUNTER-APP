package com.example.duroodcounter.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.duroodcounter.R
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class DuroodReminderWorker(context: Context, params: WorkerParameters) :
    Worker(context, params) {

    override fun doWork(): Result {
        showNotification("Durood Reminder", "Take a moment to send Durood upon the Prophet ﷺ")
        return Result.success()
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "durood_reminder_channel"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create channel (for Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Durood Reminder", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notification) // Replace with your icon
            .setAutoCancel(true)
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }
}

fun scheduleDuroodReminders(context: Context) {
    val workManager = WorkManager.getInstance(context)

    val now = Calendar.getInstance()

    val nextMorning = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 9)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        if (before(now)) add(Calendar.DAY_OF_YEAR, 1)
    }

    val nextEvening = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 20)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        if (before(now)) add(Calendar.DAY_OF_YEAR, 1)
    }

    val morningDelay = nextMorning.timeInMillis - now.timeInMillis
    val eveningDelay = nextEvening.timeInMillis - now.timeInMillis

    val constraints = Constraints.Builder()
        .setRequiresBatteryNotLow(true)
        .build()

    val morningRequest = PeriodicWorkRequestBuilder<DuroodReminderWorker>(24, TimeUnit.HOURS)
        .setInitialDelay(morningDelay, TimeUnit.MILLISECONDS)
        .setConstraints(constraints)
        .build()

    val eveningRequest = PeriodicWorkRequestBuilder<DuroodReminderWorker>(24, TimeUnit.HOURS)
        .setInitialDelay(eveningDelay, TimeUnit.MILLISECONDS)
        .setConstraints(constraints)
        .build()

    workManager.enqueueUniquePeriodicWork("durood_morning", ExistingPeriodicWorkPolicy.REPLACE, morningRequest)
    workManager.enqueueUniquePeriodicWork("durood_evening", ExistingPeriodicWorkPolicy.REPLACE, eveningRequest)
}

