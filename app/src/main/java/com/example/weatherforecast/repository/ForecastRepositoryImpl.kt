package com.example.weatherforecast.repository

import androidx.lifecycle.LiveData
import com.example.weatherforecast.data.db.CurrentWeatherDao
import com.example.weatherforecast.data.db.FutureWeatherDao
import com.example.weatherforecast.data.db.OneCallHourlyWeatherDao
import com.example.weatherforecast.data.entity.CurrentWeatherEntries
import com.example.weatherforecast.data.entity.FutureWeatherItems
import com.example.weatherforecast.data.entity.SpecificDetailFutureWeatherItems
import com.example.weatherforecast.data.model.current.CurrentWeatherResponse
import com.example.weatherforecast.data.model.future.FutureWeatherResponse
import com.example.weatherforecast.data.model.hourly.OneCallResponse
import com.example.weatherforecast.data.network.FORECAST_DAYS_COUNT
import com.example.weatherforecast.data.network.FutureOpenWeatherMapNetworkDataSource
import com.example.weatherforecast.data.network.OneCallWeatherMapNetworkDataSource
import com.example.weatherforecast.data.network.OpenWeatherMapNetworkDataSource
import com.example.weatherforecast.internal.UnitSystem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.weatherforecast.data.provider.LocationProvider
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZonedDateTime

const val ONE_CALL_WEATHER_REQUEST_EXCLUDED_CATEGORIES = "daily,minutely"

class ForecastRepositoryImpl(
    private val openWeatherMapNetworkDataSource: OpenWeatherMapNetworkDataSource,
    private val oneCallWeatherMapNetworkDataSource: OneCallWeatherMapNetworkDataSource,
    private val futureOpenWeatherMapNetworkDataSource: FutureOpenWeatherMapNetworkDataSource,
    private val currentWeatherDao: CurrentWeatherDao,
    private val oneCallWeatherDao: OneCallHourlyWeatherDao,
    private val futureWeatherDao: FutureWeatherDao,
    private val locationProvider: LocationProvider
) : ForecastRepository {

    init {
        openWeatherMapNetworkDataSource.currentWeatherLiveData.observeForever { currentWeatherResponse ->
            persistCurrentWeatherData(currentWeatherResponse)
        }

        futureOpenWeatherMapNetworkDataSource.futureWeatherLiveData.observeForever { futureWeatherResponse ->
            persistFutureWeatherData(futureWeatherResponse)
        }

        oneCallWeatherMapNetworkDataSource.oneCallWeatherLiveData.observeForever { oneCallWeatherResponse ->
            persistOneCallWeatherData(oneCallWeatherResponse)
        }

    }


    private fun persistCurrentWeatherData(currentWeatherResponse: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(currentWeatherResponse)
        }
    }

    private fun persistFutureWeatherData(futureWeatherResponse: FutureWeatherResponse) {
        fun deleteOldForecastData() {
            val today = LocalDateTime.now()
            futureWeatherDao.deleteOldEntries(today)
        }

        GlobalScope.launch(Dispatchers.IO) {
            deleteOldForecastData()
            val futureWeatherList = futureWeatherResponse.list
            futureWeatherDao.upsert(futureWeatherList)
        }
    }


    private fun persistOneCallWeatherData(oneCallResponse: OneCallResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            oneCallWeatherDao.upsert(oneCallResponse)
        }
    }

    override suspend fun getCurrentWeather(): LiveData<CurrentWeatherEntries> {
        return withContext(Dispatchers.IO){
            initFetchingWeather()
            return@withContext currentWeatherDao.getCurrentWeather()
        }
    }


    override suspend fun getFutureWeatherList(startDate: LocalDateTime): LiveData<List<FutureWeatherItems>> {
        return withContext(Dispatchers.IO) {
            initFetchingWeather()
            return@withContext futureWeatherDao.getFutureWeather(startDate)
        }
    }

    override suspend fun getFutureWeatherByDate(date: LocalDateTime): LiveData<out SpecificDetailFutureWeatherItems> {
        return withContext(Dispatchers.IO) {
            initFetchingWeather()
            return@withContext futureWeatherDao.getDetailedWeatherByDate(date)
        }
    }

    private suspend fun initFetchingWeather() {
        val lastWeatherLocation = currentWeatherDao.getCurrentWeatherNonLive()
        if (lastWeatherLocation == null || locationProvider.hasLocationChanged(
                lastWeatherLocation
            )
        ) {
            fetchWeather()
            fetchFutureWeather()
            return
        }

        if (isFetchedCurrentWeatherDataNeeded(lastWeatherLocation.zonedDateTime)) {
            fetchWeather()
        }

        if (isFetchFutureNeeded()) {
            fetchFutureWeather()
        }

        val lat = lastWeatherLocation.coord.lat
        val lon = lastWeatherLocation.coord.lon
        fetchOneCallWeather(lat, lon)
    }

    override suspend fun getHourlyWeather(): LiveData<OneCallResponse> {
        initFetchingWeather()
        return withContext(Dispatchers.IO) {
            return@withContext oneCallWeatherDao.getOneCallWeather()
        }
    }


    private suspend fun fetchWeather() {
        locationProvider.getPreferredLocationString()?.let {
            openWeatherMapNetworkDataSource.fetchCurrentWeather(
                it,
                UnitSystem.IMPERIAL.name
            )
        }
    }


    private suspend fun fetchFutureWeather() {
        locationProvider.getPreferredLocationString()?.let {
            futureOpenWeatherMapNetworkDataSource.fetchFutureWeather(
                it,
                UnitSystem.IMPERIAL.name
            )
        }
    }


    private suspend fun fetchOneCallWeather(lat: Double, lon: Double) {
        oneCallWeatherMapNetworkDataSource.fetchOneCallWeather(
            lat,
            lon,
            ONE_CALL_WEATHER_REQUEST_EXCLUDED_CATEGORIES,
            UnitSystem.IMPERIAL.name
        )
    }

    private fun isFetchedCurrentWeatherDataNeeded(lastFetchedTime: ZonedDateTime): Boolean {
        val halfAnHourAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchedTime.isBefore(halfAnHourAgo)
    }

    private fun isFetchFutureNeeded(): Boolean {
        val today = LocalDateTime.now()
        val futureWeatherCount = futureWeatherDao.countFutureWeather(today)
        return futureWeatherCount < FORECAST_DAYS_COUNT
    }
}