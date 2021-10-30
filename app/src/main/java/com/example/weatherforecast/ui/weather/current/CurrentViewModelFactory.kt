package com.example.weatherforecast.ui.weather.current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.data.provider.NotificationProvider
import com.example.weatherforecast.data.provider.UnitProvider
import com.example.weatherforecast.repository.ForecastRepository

@Suppress("UNCHECKED_CAST")
class CurrentViewModelFactory(
    private val forecastRepository: ForecastRepository,
    private val unitProvider: UnitProvider,
    private val notificationProvider: NotificationProvider
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentViewModel(forecastRepository, unitProvider, notificationProvider) as T
    }
}