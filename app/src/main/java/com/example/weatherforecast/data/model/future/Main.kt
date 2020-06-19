package com.example.weatherforecast.data.model.future

import com.google.gson.annotations.SerializedName

data class Main(
    val temp: Double = 0.0,
    @SerializedName("feels_like")
    val feelsLike: Double = 0.0,
    @SerializedName("temp_min")
    val tempMin: Double = 0.0,
    @SerializedName("temp_max")
    val tempMax: Double = 0.0,
    val pressure: Int = 0,
    val humidity: Int = 0
)