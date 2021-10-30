package com.example.weatherforecast.data.network

import android.content.Context
import com.example.weatherforecast.data.model.current.CurrentWeatherResponse
import com.example.weatherforecast.data.model.future.FutureWeatherResponse
import com.example.weatherforecast.data.model.hourly.OneCallResponse
import com.example.weatherforecast.internal.getBaseUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapApiService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") location: String,
        @Query("units") units: String
    ): Response<CurrentWeatherResponse>

    @GET("onecall")
    suspend fun getOneCall(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String,
        @Query("units") units: String
    ): Response<OneCallResponse>

    @GET("forecast")
    suspend fun getFutureWeather(
        @Query("q") location: String,
        @Query("cnt") numberDaysReturned: Int,
        @Query("units") units: String
    ): Response<FutureWeatherResponse>

    companion object {
        private const val API_KEY = ""
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor,
            context: Context
        ): OpenWeatherMapApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter(
                        "appid",
                        apiKeyString()
                    )
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient
                .Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(getBaseUrl(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenWeatherMapApiService::class.java)
        }

        private fun apiKeyString(): String {
            if (API_KEY.isEmpty()){
                throw RuntimeException("Insert your API KEY")
            }else{
                return API_KEY
            }
        }
    }
}