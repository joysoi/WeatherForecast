package com.example.weatherforecast.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherforecast.internal.TestUtil
import com.example.weatherforecast.internal.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CurrentWeatherDaoTest : ForecastDatabaseTest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun upsertAndShowWeatherForLocation_TrueIfLocationMatch() {
        val upsertWeatherResponseForLocation = TestUtil.createWeatherResponseForLocationName("New York")
        runBlocking {
            db.currentWeatherDao().upsert(upsertWeatherResponseForLocation)
        }
        val loaded = db.currentWeatherDao().getCurrentWeather().getOrAwaitValue()
        MatcherAssert.assertThat(loaded.name, CoreMatchers.`is`("New York"))

        // synchronous data source
        val loadedNonLive = db.currentWeatherDao().getCurrentWeatherNonLive()
        MatcherAssert.assertThat(loadedNonLive?.name, CoreMatchers.`is`("New York"))

    }
}