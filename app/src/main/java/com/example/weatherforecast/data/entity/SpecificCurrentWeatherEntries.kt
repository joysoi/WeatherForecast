package com.example.weatherforecast.data.entity
import com.example.weatherforecast.data.model.current.Weather

interface SpecificCurrentWeatherEntries {
    val name: String
    val temperature: Double
    val maxTemperature: Double
    val minTemperature: Double
    val entries: List<Weather>
}