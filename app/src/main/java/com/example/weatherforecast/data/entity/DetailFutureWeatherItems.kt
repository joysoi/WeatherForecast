package com.example.weatherforecast.data.entity

import androidx.room.ColumnInfo
import com.example.weatherforecast.data.model.future.FutureWeatherDays
import org.threeten.bp.LocalDateTime

class DetailFutureWeatherItems(
    @ColumnInfo(name = "dtTxt")
    override val date: LocalDateTime,
    @ColumnInfo(name = "main_temp")
    override val temperature: Double,
    @ColumnInfo(name = "main_tempMax")
    override val maxTemperature: Double,
    @ColumnInfo(name = "main_tempMin")
    override val minTemperature: Double,
    @ColumnInfo(name = "main_feelsLike")
    override val feelsLike: Double,
    @ColumnInfo(name = "entries")
    override val entries: List<FutureWeatherDays>,
    @ColumnInfo(name = "main_pressure")
    override val pressure: Int,
    @ColumnInfo(name = "main_humidity")
    override val humidity: Int
) : SpecificDetailFutureWeatherItems