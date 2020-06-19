package com.example.weatherforecast.repository

import androidx.lifecycle.LiveData
import com.example.weatherforecast.data.entity.CurrentWeatherEntries
import com.example.weatherforecast.data.entity.FutureWeatherItems
import com.example.weatherforecast.data.entity.SpecificDetailFutureWeatherItems
import com.example.weatherforecast.data.model.hourly.OneCallResponse
import org.threeten.bp.LocalDateTime

interface ForecastRepository{
    suspend fun getCurrentWeather(): LiveData<CurrentWeatherEntries>
    suspend fun getFutureWeatherList(startDate: LocalDateTime): LiveData<List<FutureWeatherItems>>
    suspend fun getFutureWeatherByDate(date: LocalDateTime): LiveData<out SpecificDetailFutureWeatherItems>
    suspend fun getHourlyWeather(): LiveData<OneCallResponse>
}