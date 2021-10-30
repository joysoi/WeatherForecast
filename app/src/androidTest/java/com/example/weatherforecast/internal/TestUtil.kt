package com.example.weatherforecast.internal

import com.example.weatherforecast.data.model.current.Coord
import com.example.weatherforecast.data.model.current.CurrentWeatherResponse
import com.example.weatherforecast.data.model.future.FutureWeatherEntry

object TestUtil {
    fun createWeatherResponseForLocationName(locationName: String) = CurrentWeatherResponse(
        name = locationName
    )

    fun createHourlyWeatherResponseUsingLocationCoordinates(lon: Double, lat: Double) =
        CurrentWeatherResponse(
            coord = Coord(lon, lat)
        )

    fun createFutureWeatherEntryList(date: String): List<FutureWeatherEntry> {
       return (0 until 6).map {
           FutureWeatherEntry(
               dtTxt = date
           )
       }
    }

}

