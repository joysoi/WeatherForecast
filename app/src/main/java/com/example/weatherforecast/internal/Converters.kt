package com.example.weatherforecast.internal

import androidx.room.TypeConverter
import com.example.weatherforecast.data.model.current.Weather
import com.example.weatherforecast.data.model.future.FutureWeatherDays
import com.example.weatherforecast.data.model.future.FutureWeatherEntry
import com.example.weatherforecast.data.model.hourly.Hourly
import com.example.weatherforecast.data.model.hourly.WeatherX
import com.google.gson.Gson
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class Converters {

    @TypeConverter
    fun listWeatherToJson(value: List<Weather>?): String? = Gson().toJson(value)

    @TypeConverter
    fun jsonToWeatherList(value: String) =
        Gson().fromJson(value, Array<Weather>::class.java).toList()

    @TypeConverter
    fun listHourlyToJson(value: List<Hourly>?): String? = Gson().toJson(value)

    @TypeConverter
    fun jsonToHourlyList(value: String) = Gson().fromJson(value, Array<Hourly>::class.java).toList()

    @TypeConverter
    fun listFutureWeatherDaysToJson(value: List<FutureWeatherDays>?): String? = Gson().toJson(value)

    @TypeConverter
    fun jsonToFutureWeatherDaysList(value: String) =
        Gson().fromJson(value, Array<FutureWeatherDays>::class.java).toList()

    @TypeConverter
    fun stringToLocalDateTime(str: String?) = str?.let {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.of("UTC"))
        LocalDateTime.parse(str, formatter)
    }

    @TypeConverter
    fun localDateTimeToString(dateTime: LocalDateTime?) =
        dateTime?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

}
