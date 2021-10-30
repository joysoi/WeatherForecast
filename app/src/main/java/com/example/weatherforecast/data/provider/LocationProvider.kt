package com.example.weatherforecast.data.provider

import com.example.weatherforecast.data.model.current.CurrentWeatherResponse

interface LocationProvider {
    suspend fun hasLocationChanged(currentWeatherResponse: CurrentWeatherResponse): Boolean
    suspend fun getPreferredLocationString(): String?
}