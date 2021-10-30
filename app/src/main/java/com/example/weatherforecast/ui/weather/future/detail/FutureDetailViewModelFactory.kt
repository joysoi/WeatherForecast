package com.example.weatherforecast.ui.weather.future.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.data.provider.UnitProvider
import com.example.weatherforecast.repository.ForecastRepository
import org.threeten.bp.LocalDateTime

@Suppress("UNCHECKED_CAST")
class FutureDetailViewModelFactory(
    private val detailDate: LocalDateTime,
    private val forecastRepository: ForecastRepository,
    private val unitProvider: UnitProvider
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FutureDetailWeatherViewModel(
            detailDate,
            forecastRepository,
            unitProvider
        ) as T
    }
}