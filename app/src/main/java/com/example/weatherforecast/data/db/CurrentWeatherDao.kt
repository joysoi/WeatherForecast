package com.example.weatherforecast.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.data.entity.CurrentWeatherEntries
import com.example.weatherforecast.data.model.current.CURRENT_WEATHER_ID
import com.example.weatherforecast.data.model.current.CurrentWeatherResponse

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(currentWeatherResponse: CurrentWeatherResponse)

    @Query("select * from current_weather where pk = $CURRENT_WEATHER_ID")
    fun getCurrentWeather(): LiveData<CurrentWeatherEntries>

    @Query("select * from current_weather where pk = $CURRENT_WEATHER_ID")
    fun getCurrentWeatherNonLive(): CurrentWeatherResponse?

}