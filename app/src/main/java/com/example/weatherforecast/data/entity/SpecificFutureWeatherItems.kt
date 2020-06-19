package com.example.weatherforecast.data.entity

import com.example.weatherforecast.data.model.future.FutureWeatherDays

interface SpecificFutureWeatherItems {
    val date: String
    val temperature: Double
    val entries: List<FutureWeatherDays>
}