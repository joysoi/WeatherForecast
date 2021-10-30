package com.example.weatherforecast.internal

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.weatherforecast.CHANNEL_ID
import com.example.weatherforecast.R
import com.example.weatherforecast.ui.MainActivity

class UpdateWeatherService : Service() {

    companion object {
        fun startService(context: Context, locationName: String, description: String, temp: Int) {
            val startIntent = Intent(context, UpdateWeatherService::class.java)
            startIntent.putExtra("locationName", locationName)
            startIntent.putExtra("locationDescription", description)
            startIntent.putExtra("locationTemp", temp)
            ContextCompat.startForegroundService(context, startIntent)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, UpdateWeatherService::class.java)
            context.stopService(stopIntent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val locationInput = intent?.getStringExtra("locationName")
        val descriptionInput = intent?.getStringExtra("locationDescription")
        val tempInput = intent?.getIntExtra("locationTemp", 71)
        startForeground(1, createNotification(locationInput, descriptionInput, tempInput))
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotification(
        locationName: String?,
        description: String?,
        temp: Int?
    ): Notification {
        val intent =
            Intent(this, MainActivity::class.java)
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_sunny)
            .setContentTitle(locationName)
            .setContentText("$description \u2022 $temp°")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)

        return builder.build()
    }
}