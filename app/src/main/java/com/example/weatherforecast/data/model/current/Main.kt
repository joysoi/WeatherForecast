package com.example.weatherforecast.data.model.current

import com.google.gson.annotations.SerializedName

data class Main(
    val temp: Double = 0.0,
    @SerializedName("temp_min")
    val tempMin: Double = 0.0,
    @SerializedName("temp_max")
    val tempMax: Double = 0.0
)