package com.example.weatherforecast.data.provider

import android.content.Context
import com.example.weatherforecast.internal.UnitSystem

const val UNIT_SYSTEM = "UNIT_SYSTEM"

class UnitProviderImpl(context: Context) : PreferenceProvider(context), UnitProvider {

    override fun getUnitSystem(): UnitSystem {
        val selectedName = preferences.getString(UNIT_SYSTEM, UnitSystem.IMPERIAL.name)
        return UnitSystem.valueOf(selectedName!!)
    }
}