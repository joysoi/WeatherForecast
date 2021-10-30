package com.example.weatherforecast.ui.weather.future.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.data.provider.UnitProvider
import com.example.weatherforecast.repository.ForecastRepository

@Suppress("UNCHECKED_CAST")
class FutureViewModelFactory(
    private val forecastRepository: ForecastRepository,
    private val unitProvider: UnitProvider
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FutureListViewModel(
            forecastRepository,
            unitProvider
        ) as T
    }
}