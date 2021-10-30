package com.example.weatherforecast.data.network

import androidx.lifecycle.LiveData
import com.example.weatherforecast.data.model.current.CurrentWeatherResponse

interface OpenWeatherMapNetworkDataSource {
    val currentWeatherLiveData: LiveData<CurrentWeatherResponse>
    suspend fun fetchCurrentWeather(location: String, units: String)
}