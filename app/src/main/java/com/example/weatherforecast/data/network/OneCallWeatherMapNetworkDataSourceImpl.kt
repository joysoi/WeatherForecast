package com.example.weatherforecast.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherforecast.data.model.hourly.OneCallResponse
import com.example.weatherforecast.internal.NoConnectivityException
import kotlinx.coroutines.withTimeout
import java.util.concurrent.CancellationException

class OneCallWeatherMapNetworkDataSourceImpl(
    private val openWeatherMapApiService: OpenWeatherMapApiService
) : OneCallWeatherMapNetworkDataSource {
    private val _oneCallWeatherLiveData = MutableLiveData<OneCallResponse>()
    override val oneCallWeatherLiveData: LiveData<OneCallResponse>
        get() = _oneCallWeatherLiveData

    override suspend fun fetchOneCallWeather(
        lat: Double,
        lon: Double,
        exclude: String,
        units: String
    ) {
        try {
            withTimeout(5000) {
                val result = openWeatherMapApiService.getOneCall(
                    lat, lon, exclude, units
                )
                if (result.isSuccessful) {
                    result.body()?.let {
                        _oneCallWeatherLiveData.postValue(it)
                    }
                } else {
                    when (result.code()) {
                        404 -> Log.d("OneCallNetworkSource", "Not Fond")
                        500 -> Log.d("OneCallNetworkSource", "Broken server")
                        else -> Log.d("OneCallNetworkSource", "Unknown Error")
                    }
                }
            }
        } catch (e: NoConnectivityException) {
            Log.d("OneCallNetworkSource", "No Internet", e)
        } catch (e: CancellationException) {
            Log.d("OneCallNetworkSource", "Timed out", e)
        }
    }
}