package com.example.weatherforecast.ui.weather.future.list

import com.example.weatherforecast.data.provider.UnitProvider
import com.example.weatherforecast.internal.lazyDeferred
import com.example.weatherforecast.repository.ForecastRepository
import com.example.weatherforecast.ui.base.WeatherViewModel
import org.threeten.bp.LocalDateTime

class FutureListViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    val weatherEntries by lazyDeferred {
        forecastRepository.getFutureWeatherList(LocalDateTime.now())
    }
}