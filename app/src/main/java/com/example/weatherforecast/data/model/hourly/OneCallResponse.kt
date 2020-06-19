package com.example.weatherforecast.data.model.hourly

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val ONECALL_WEATHER_ID = 0

@Entity(tableName = "onecall_weather")
data class OneCallResponse(
    val timezone: String = "",
    @SerializedName("hourly")
    val hourly: List<Hourly> = listOf()
) {
    @PrimaryKey(autoGenerate = false)
    var pk: Int? = ONECALL_WEATHER_ID

}