package com.example.weatherforecast.data.model.future

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class FutureWeatherResponse(
    @SerializedName("list")
    val list: List<FutureWeatherEntry> = listOf()
)