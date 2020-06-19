package com.example.weatherforecast.data.model.future

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "future_weather", indices = [Index(value = ["dtTxt"], unique = true)])
data class FutureWeatherEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @Embedded(prefix = "main_")
    val main: Main = Main(),
    @SerializedName("weather")
    val entries: List<FutureWeatherDays> = listOf(),
    @SerializedName("dt_txt")
    val dtTxt: String = ""
)