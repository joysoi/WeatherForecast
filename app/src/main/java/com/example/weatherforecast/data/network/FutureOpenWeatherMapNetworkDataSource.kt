package com.example.weatherforecast.data.network

import androidx.lifecycle.LiveData
import com.example.weatherforecast.data.model.future.FutureWeatherResponse

interface FutureOpenWeatherMapNetworkDataSource {
    val futureWeatherLiveData: LiveData<FutureWeatherResponse>
    suspend fun fetchFutureWeather(
        location: String,
        units: String
    )
}