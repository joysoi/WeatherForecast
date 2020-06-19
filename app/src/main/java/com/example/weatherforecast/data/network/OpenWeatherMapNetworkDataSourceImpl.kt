package com.example.weatherforecast.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherforecast.data.model.current.CurrentWeatherResponse
import com.example.weatherforecast.internal.NoConnectivityException
import kotlinx.coroutines.withTimeout
import java.util.concurrent.CancellationException

class OpenWeatherMapNetworkDataSourceImpl(
    private val openWeatherMapApiService: OpenWeatherMapApiService
) : OpenWeatherMapNetworkDataSource {
    private val _currentWeatherLiveData = MutableLiveData<CurrentWeatherResponse>()
    override val currentWeatherLiveData: LiveData<CurrentWeatherResponse>
        get() = _currentWeatherLiveData

    override suspend fun fetchCurrentWeather(location: String, units: String) {
        try {
            withTimeout(5000) {
                val result = openWeatherMapApiService.getCurrentWeather(location, units)
                if (result.isSuccessful) {
                    result.body()?.let {
                        _currentWeatherLiveData.postValue(it)
                    }
                } else {
                    when (result.code()) {
                        404 -> {
                            Log.d("NetworkDataSourceImpl", "Server not found")
                        }
                        500 -> {
                            Log.e("NetworkDataSourceImpl", "server broken")
                        }
                        else -> {
                            Log.e("NetworkDataSourceImpl", "unknown error")
                        }
                    }
                }
            }
        } catch (e: CancellationException) {
            Log.e("NetworkDataSourceImpl", "Timed out", e)
        } catch (e: NoConnectivityException) {
            Log.e("NetworkDataSourceImpl", "NO Internet!", e)
        }
    }
}