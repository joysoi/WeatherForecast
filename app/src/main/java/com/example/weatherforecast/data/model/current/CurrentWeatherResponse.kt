package com.example.weatherforecast.data.model.current

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

const val CURRENT_WEATHER_ID = 0

@Entity(tableName = "current_weather")
data class CurrentWeatherResponse(
    @Embedded(prefix = "coord_")
    val coord: Coord = Coord(),
    val weather: List<Weather> = listOf(),
    @Embedded(prefix = "main_")
    val main: Main = Main(),
    val dt: Int = 0,
    val timezone: Int = 0,
    val name: String = ""
) {
    @PrimaryKey(autoGenerate = false)
    var pk: Int =
        CURRENT_WEATHER_ID

    val zonedDateTime: ZonedDateTime
        get() {
            val instant = Instant.ofEpochSecond(dt.toLong())
            val offset = ZoneOffset.ofTotalSeconds(timezone)
            val zoneId = ZoneId.ofOffset("UTC", offset)
            return ZonedDateTime.ofInstant(instant, zoneId)
        }
}