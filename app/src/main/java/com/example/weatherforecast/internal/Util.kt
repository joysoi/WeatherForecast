package com.example.weatherforecast.internal

import android.content.Context
import android.widget.ImageView
import com.example.weatherforecast.R
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter


fun getBaseUrl(context: Context): String{
    return context.getString(R.string.base_url)
}

fun tempToMetric(temp: Int): Int {
    return (temp - 32) * 5 / 9
}

fun stringArgsToLocalDateTime(startDate: String?): LocalDateTime? {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneId.of("UTC"))
    return LocalDateTime.parse(startDate, formatter)
}

fun updateWeatherIcon(weatherIcon: ImageView, icon: String) {
    when (icon) {
        "01d", "02d" -> {
            weatherIcon.setImageResource(R.drawable.ic_sunny)
        }
        "03d", "04d", "02n", "03n", "04n" -> {
            weatherIcon.setImageResource(R.drawable.ic_cloud)
        }
        "09d", "10d", "09n", "10n" -> {
            weatherIcon.setImageResource(R.drawable.ic_rain)
        }
        "11d", "11n" -> {
            weatherIcon.setImageResource(R.drawable.ic_thunder)
        }
        "13d", "13n" -> {
            weatherIcon.setImageResource(R.drawable.ic_snow)
        }
        "50d", "50n" -> {
            weatherIcon.setImageResource(R.drawable.ic_mist)
        }
        "01n" -> {
            weatherIcon.setImageResource(R.drawable.ic_night)
        }
    }
}