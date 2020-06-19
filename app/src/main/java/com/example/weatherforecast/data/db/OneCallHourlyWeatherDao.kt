package com.example.weatherforecast.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.data.model.hourly.ONECALL_WEATHER_ID
import com.example.weatherforecast.data.model.hourly.OneCallResponse

@Dao
interface OneCallHourlyWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(oneCallResponse: OneCallResponse)

    @Query("select * from onecall_weather where pk = $ONECALL_WEATHER_ID")
    fun getOneCallWeather(): LiveData<OneCallResponse>
}