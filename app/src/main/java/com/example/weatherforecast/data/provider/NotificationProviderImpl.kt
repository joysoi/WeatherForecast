package com.example.weatherforecast.data.provider

import android.content.Context
import com.example.weatherforecast.internal.UpdateWeatherService

const val USE_WEATHER_NOTIFICATIONS = "USE_WEATHER_NOTIFICATIONS"

class NotificationProviderImpl(
    context: Context
) : PreferenceProvider(context), NotificationProvider {

    private val appContext = context.applicationContext

    override fun showNotificationForCurrentWeatherLocation(
        locationName: String,
        descriptionWeather: String,
        tempLocation: Int
    ) {
        if (isUsingWeatherNotification()) {
            UpdateWeatherService.startService(appContext, locationName, descriptionWeather, tempLocation)
        } else {
            UpdateWeatherService.stopService(appContext)
        }
    }

    private fun isUsingWeatherNotification(): Boolean {
        return preferences.getBoolean(USE_WEATHER_NOTIFICATIONS, true)
    }
}