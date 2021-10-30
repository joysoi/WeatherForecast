package com.example.weatherforecast.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherforecast.data.network.FORECAST_DAYS_COUNT
import com.example.weatherforecast.internal.TestUtil
import com.example.weatherforecast.internal.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.LocalDateTime

@RunWith(AndroidJUnit4::class)
class FutureWeatherDaoTest : ForecastDatabaseTest() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun upsertWeatherForNewLocation_returnTrueIfFromToday() {
        val today: LocalDateTime = LocalDateTime.now()
        val futureList =
            TestUtil.createFutureWeatherEntryList(today.toString())
        runBlocking {
            db.futureWeatherDao().upsert(futureList)
        }

        val loadedDate = db.futureWeatherDao().getFutureWeather(today).getOrAwaitValue()
        val currentDay = loadedDate[0].date

        assertThat(currentDay, Matchers.equalTo(today.toString()))
    }

    @Test
    fun deleteOldWeatherEntriesListToday_trueIfListEmpty() {
        val today: LocalDateTime = LocalDateTime.now()
        val yesterday: LocalDateTime = LocalDateTime.now().minusDays(1)
        val yesterdaysWeatherData = TestUtil.createFutureWeatherEntryList(yesterday.toString())
        runBlocking {
            db.futureWeatherDao().upsert(yesterdaysWeatherData)
        }
        db.futureWeatherDao().deleteOldEntries(today)
        val loaded = db.futureWeatherDao().getFutureWeather(yesterday).getOrAwaitValue()
        assertThat(loaded.isNullOrEmpty(), Matchers.`is`(true))
    }


    @Test
    fun detailWeatherForToday_trueIfDetailIsFromToday() {
        val today = LocalDateTime.now()
        val todayDetailWeatherList = TestUtil.createFutureWeatherEntryList(today.toString())
        runBlocking {
            db.futureWeatherDao().upsert(todayDetailWeatherList)
        }

        val futureWeatherList = db.futureWeatherDao()
            .getFutureWeather(today).getOrAwaitValue { }
        val todayDetailWeather = futureWeatherList[0].date

        assertThat(todayDetailWeather, Matchers.`is`(today.toString()))
    }

    @Test
    fun numberOfDaysPresented_TrueIfLessThenSevenDays() {
        val today = LocalDateTime.now()
        runBlocking {
            db.futureWeatherDao()
                .upsert(TestUtil.createFutureWeatherEntryList(today.toString()))
        }

        db.futureWeatherDao().countFutureWeather(today)
        val loadedData = db.futureWeatherDao().getFutureWeather(today).getOrAwaitValue()
        val sevenDaysCount = FORECAST_DAYS_COUNT
        assertThat(loadedData[0].date, Matchers.lessThanOrEqualTo(sevenDaysCount.toString()))
    }
}