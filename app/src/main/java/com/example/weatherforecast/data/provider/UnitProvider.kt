package com.example.weatherforecast.data.provider

import com.example.weatherforecast.internal.UnitSystem

interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}