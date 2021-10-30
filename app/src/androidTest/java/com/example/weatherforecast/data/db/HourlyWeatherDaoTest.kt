package com.example.weatherforecast.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherforecast.internal.TestUtil
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.abs


@RunWith(AndroidJUnit4::class)
class HourlyWeatherDaoTest : ForecastDatabaseTest(){

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun coordinatesMatchForLocation_trueIfCoordinatesMatch() {

        val locationResponseLatLon =
            TestUtil.createHourlyWeatherResponseUsingLocationCoordinates(abs(-74.01), 40.71)
        runBlocking {
            db.currentWeatherDao().upsert(locationResponseLatLon)
        }

        val loadedNonLiveLon = db.currentWeatherDao().getCurrentWeatherNonLive()?.coord?.lon
        val loadedNonLiveLat = db.currentWeatherDao().getCurrentWeatherNonLive()?.coord?.lat
        MatcherAssert.assertThat(loadedNonLiveLon, Matchers.closeTo(abs(-74.01), abs(-74.00)))
        MatcherAssert.assertThat(loadedNonLiveLat, Matchers.closeTo(40.71, 40.70))
    }
}