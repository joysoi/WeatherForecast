package com.example.weatherforecast.data.model.hourly

import com.google.gson.annotations.SerializedName

data class Hourly(
    val dt: Int = 0,
    val temp: Double = 0.0,
    @SerializedName("weather")
    val weather: List<WeatherX> = listOf()
)
