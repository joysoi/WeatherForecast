package com.example.weatherforecast.data.model.future


import com.google.gson.annotations.SerializedName

data class City(
    val id: Int = 0,
    val name: String = "",
    val country: String = "",
    val timezone: Int = 0
)