package com.example.weatherforecast.data.provider

interface NotificationProvider {
    fun showNotificationForCurrentWeatherLocation(
        locationName: String,
        descriptionWeather: String,
        tempLocation: Int
    )
}