package com.example.weatherforecast.data.entity

import androidx.room.ColumnInfo
import com.example.weatherforecast.data.model.future.FutureWeatherDays

class FutureWeatherItems(
    @ColumnInfo(name = "dtTxt")
    override val date: String,
    @ColumnInfo(name = "main_temp")
    override val temperature: Double,
    @ColumnInfo(name = "entries")
    override val entries: List<FutureWeatherDays>
) : SpecificFutureWeatherItems