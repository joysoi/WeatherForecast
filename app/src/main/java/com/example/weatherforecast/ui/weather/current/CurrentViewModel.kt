package com.example.weatherforecast.ui.weather.current

import androidx.lifecycle.ViewModel
import com.example.weatherforecast.data.provider.NotificationProvider
import com.example.weatherforecast.data.provider.UnitProvider
import com.example.weatherforecast.internal.lazyDeferred
import com.example.weatherforecast.repository.ForecastRepository
import com.example.weatherforecast.ui.base.WeatherViewModel

class CurrentViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider,
    notificationProvider: NotificationProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    val oneCallHourlyWeather by lazyDeferred {
        forecastRepository.getHourlyWeather()
    }

    val notification = notificationProvider
}
