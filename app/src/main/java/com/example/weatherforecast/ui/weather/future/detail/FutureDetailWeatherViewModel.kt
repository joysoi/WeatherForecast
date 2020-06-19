package com.example.weatherforecast.ui.weather.future.detail

import com.example.weatherforecast.data.provider.UnitProvider
import com.example.weatherforecast.internal.lazyDeferred
import com.example.weatherforecast.repository.ForecastRepository
import com.example.weatherforecast.ui.base.WeatherViewModel
import org.threeten.bp.LocalDateTime

class FutureDetailWeatherViewModel(
    private val detailDate: LocalDateTime,
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {
    val detailWeather by lazyDeferred {
        forecastRepository.getFutureWeatherByDate(detailDate)
    }
}
