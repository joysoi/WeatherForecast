package com.example.weatherforecast.data.entity

import androidx.room.ColumnInfo
import com.example.weatherforecast.data.model.current.Weather

class CurrentWeatherEntries(
    @ColumnInfo(name = "name")
    override val name: String,
    @ColumnInfo(name = "main_temp")
    override val temperature: Double,
    @ColumnInfo(name = "main_tempMax")
    override val maxTemperature: Double,
    @ColumnInfo(name = "main_tempMin")
    override val minTemperature: Double,
    @ColumnInfo(name = "weather")
    override val entries: List<Weather>
) : SpecificCurrentWeatherEntries