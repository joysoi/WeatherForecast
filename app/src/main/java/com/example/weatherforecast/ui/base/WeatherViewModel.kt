package com.example.weatherforecast.ui.base

import androidx.lifecycle.ViewModel
import com.example.weatherforecast.data.provider.UnitProvider
import com.example.weatherforecast.internal.UnitSystem
import com.example.weatherforecast.internal.lazyDeferred
import com.example.weatherforecast.repository.ForecastRepository

abstract class WeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()

    val isImperial: Boolean
        get() = unitSystem == UnitSystem.IMPERIAL

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather()
    }
}