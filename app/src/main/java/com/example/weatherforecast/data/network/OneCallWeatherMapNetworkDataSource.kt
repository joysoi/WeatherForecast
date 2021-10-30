package com.example.weatherforecast.data.network

import androidx.lifecycle.LiveData
import com.example.weatherforecast.data.model.hourly.OneCallResponse

interface OneCallWeatherMapNetworkDataSource {
    val oneCallWeatherLiveData: LiveData<OneCallResponse>
    suspend fun fetchOneCallWeather(lat: Double, lon: Double, exclude: String, units: String)
}