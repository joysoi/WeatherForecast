package com.example.weatherforecast.data.provider

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.weatherforecast.data.model.current.CurrentWeatherResponse
import com.example.weatherforecast.internal.LocationPermissionNotGrantedException
import com.example.weatherforecast.internal.asDeferredAsync
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred
import java.util.*
import kotlin.math.abs

const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"

class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
) : PreferenceProvider(context), LocationProvider {

    private val appContext = context.applicationContext

    override suspend fun hasLocationChanged(currentWeatherResponse: CurrentWeatherResponse): Boolean {
        val deviceLocationChanged = try {
            hasDeviceLocationChanged(currentWeatherResponse)
        } catch (e: LocationPermissionNotGrantedException) {
            false
        }
        return deviceLocationChanged || hasCustomLocationChanged(currentWeatherResponse)
    }

    override suspend fun getPreferredLocationString(): String? {
        if (isUsingDeviceLocation()) {
            try {
                val deviceLocation = getLastDeviceLocationAsync().await()
                    ?: return "${getCustomLocationName()}"
                val latDevice = deviceLocation.latitude
                val lonDevice = deviceLocation.longitude
                return getCityName(latDevice, lonDevice, appContext)
            } catch (e: LocationPermissionNotGrantedException) {
                return "${getCustomLocationName()}"
            }
        } else
            return "${getCustomLocationName()}"
    }

    private fun getCityName(lat: Double, lon: Double, context: Context): String? {
        var cityName: String? = null
        val geocode = Geocoder(context, Locale.getDefault())
        val addressList: List<Address> = geocode.getFromLocation(lat, lon, 1)
        if (!addressList.isNullOrEmpty()) {
            cityName = addressList[0].locality
            cityName = if (!cityName.isNullOrEmpty()) {
                addressList[0].locality
            } else {
                addressList[0].adminArea
            }
        } else {
            Log.d("LocationProvider", "Address list empty")
        }
        return cityName
    }

    private suspend fun hasDeviceLocationChanged(currentWeatherResponse: CurrentWeatherResponse): Boolean {
        if (!isUsingDeviceLocation())
            return false

        val deviceLocation = getLastDeviceLocationAsync().await()
            ?: return false

        val comparisonThreshold = 0.03
        return abs(deviceLocation.latitude - currentWeatherResponse.coord.lat) > comparisonThreshold &&
                abs(deviceLocation.longitude - currentWeatherResponse.coord.lon) > comparisonThreshold
    }

    private fun hasCustomLocationChanged(currentWeatherResponse: CurrentWeatherResponse): Boolean {
        if (!isUsingDeviceLocation()) {
            val customLocationName = getCustomLocationName()
            return customLocationName != currentWeatherResponse.name
        }
        return false
    }

    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    private fun getCustomLocationName(): String? {
        return preferences.getString(CUSTOM_LOCATION, null)
    }

    private fun getLastDeviceLocationAsync(): Deferred<Location?> {
        return if (hasLocationPermission())
            fusedLocationProviderClient.lastLocation.asDeferredAsync()
        else
            throw LocationPermissionNotGrantedException()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}