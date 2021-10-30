package com.example.weatherforecast

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.preference.PreferenceManager
import com.example.weatherforecast.data.db.ForecastDatabase
import com.example.weatherforecast.data.network.*
import com.example.weatherforecast.data.provider.*
import com.example.weatherforecast.repository.ForecastRepository
import com.example.weatherforecast.repository.ForecastRepositoryImpl
import com.example.weatherforecast.ui.weather.current.CurrentViewModelFactory
import com.example.weatherforecast.ui.weather.future.detail.FutureDetailViewModelFactory
import com.example.weatherforecast.ui.weather.future.list.FutureViewModelFactory
import com.facebook.stetho.Stetho
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*
import org.threeten.bp.LocalDateTime


const val CHANNEL_ID = "weatherChannel"
const val CHANNEL_NAME = "weather update channel"
const val CHANNEL_DESCRIPTION = "Periodically updating the weather"

class WeatherForecastApp : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@WeatherForecastApp))
        bind() from singleton { ForecastDatabase(instance()) }
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().oneCallHourlyWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().futureWeatherDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { OpenWeatherMapApiService(instance(), instance()) }
        bind<OpenWeatherMapNetworkDataSource>() with singleton {
            OpenWeatherMapNetworkDataSourceImpl(
                instance()
            )
        }
        bind<OneCallWeatherMapNetworkDataSource>() with singleton {
            OneCallWeatherMapNetworkDataSourceImpl(
                instance()
            )
        }

        bind<FutureOpenWeatherMapNetworkDataSource>() with singleton {
            FutureOpenWeatherMapNetworkDataSourceImpl(
                instance()
            )
        }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }
        bind<NotificationProvider>() with singleton { NotificationProviderImpl(instance()) }
        bind<ForecastRepository>() with singleton {
            ForecastRepositoryImpl(
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                instance()
            )
        }
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind() from provider { CurrentViewModelFactory(instance(), instance(), instance()) }
        bind() from provider {
            FutureViewModelFactory(
                instance(),
                instance()
            )
        }
        bind() from factory { detailDate: LocalDateTime ->
            FutureDetailViewModelFactory(
                detailDate,
                instance(),
                instance()
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        Stetho.initializeWithDefaults(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
        createNotificationChannel()
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_NAME
            val descriptionText = CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}