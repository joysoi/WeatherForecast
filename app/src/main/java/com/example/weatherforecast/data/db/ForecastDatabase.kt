package com.example.weatherforecast.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherforecast.data.model.current.CurrentWeatherResponse
import com.example.weatherforecast.data.model.future.FutureWeatherEntry
import com.example.weatherforecast.data.model.hourly.OneCallResponse
import com.example.weatherforecast.internal.Converters

@Database(
    entities = [CurrentWeatherResponse::class, OneCallResponse::class, FutureWeatherEntry::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ForecastDatabase : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun oneCallHourlyWeatherDao(): OneCallHourlyWeatherDao
    abstract fun futureWeatherDao(): FutureWeatherDao

    companion object {
        @Volatile
        var INSTANCE: ForecastDatabase? = null

        operator fun invoke(context: Context): ForecastDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ForecastDatabase::class.java,
                        "forecast.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}