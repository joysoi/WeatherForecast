package com.example.weatherforecast.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherforecast.data.model.future.FutureWeatherResponse
import com.example.weatherforecast.internal.NoConnectivityException
import kotlinx.coroutines.withTimeout
import java.util.concurrent.CancellationException

const val FORECAST_DAYS_COUNT = 7

class FutureOpenWeatherMapNetworkDataSourceImpl(
    private val openWeatherMapApiService: OpenWeatherMapApiService
) : FutureOpenWeatherMapNetworkDataSource {
    private val _futureWeatherLiveData = MutableLiveData<FutureWeatherResponse>()
    override val futureWeatherLiveData: LiveData<FutureWeatherResponse>
        get() = _futureWeatherLiveData

    override suspend fun fetchFutureWeather(
        location: String,
        units: String
    ) {
        try {
            withTimeout(5000) {
                val result =
                    openWeatherMapApiService.getFutureWeather(location, FORECAST_DAYS_COUNT, units)
                if (result.isSuccessful) {
                    result.body()?.let {
                        _futureWeatherLiveData.postValue(it)
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