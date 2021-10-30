package com.example.weatherforecast.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.data.entity.DetailFutureWeatherItems
import com.example.weatherforecast.data.entity.FutureWeatherItems
import com.example.weatherforecast.data.model.future.FutureWeatherEntry
import org.threeten.bp.LocalDateTime

@Dao
interface FutureWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(futureWeatherEntries: List<FutureWeatherEntry>)

    @Query("select * from future_weather where date(dtTxt) >= date(:startDate)")
    fun getFutureWeather(startDate: LocalDateTime): LiveData<List<FutureWeatherItems>>

    @Query("select * from future_weather where time(dtTxt) = time(:date)")
    fun getDetailedWeatherByDate(date: LocalDateTime): LiveData<DetailFutureWeatherItems>

    @Query("select count(id) from future_weather where time(dtTxt) >= time(:startDate)")
    fun countFutureWeather(startDate: LocalDateTime): Int

    @Query("delete from future_weather where time(dtTxt) < time(:firstDateToKeep)")
    fun deleteOldEntries(firstDateToKeep: LocalDateTime)

}