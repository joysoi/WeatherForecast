package com.example.weatherforecast.data.entity

import com.example.weatherforecast.data.model.future.FutureWeatherDays
import org.threeten.bp.LocalDateTime

interface SpecificDetailFutureWeatherItems {
    val date: LocalDateTime
    val temperature: Double
    val maxTemperature: Double
    val minTemperature: Double
    val feelsLike: Double
    val entries: List<FutureWeatherDays>
    val pressure: Int
    val humidity: Int
}