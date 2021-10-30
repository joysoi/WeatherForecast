package com.example.weatherforecast.ui.weather.future.detail

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import org.kodein.di.android.x.closestKodein
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

import com.example.weatherforecast.R
import com.example.weatherforecast.internal.DateNotFoundException
import com.example.weatherforecast.internal.tempToMetric
import com.example.weatherforecast.internal.updateWeatherIcon
import com.example.weatherforecast.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.future_detail_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.generic.factory
import org.threeten.bp.LocalDateTime

class FutureDetailWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactoryInstanceFactory:
            ((LocalDateTime) -> FutureDetailViewModelFactory) by factory()

    private lateinit var viewModel: FutureDetailWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_detail_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val safeArgs = arguments?.let { FutureDetailWeatherFragmentArgs.fromBundle(it) }
        val date = safeArgs?.dateString ?: throw DateNotFoundException()

        viewModel = ViewModelProviders.of(this, viewModelFactoryInstanceFactory(date))
            .get(FutureDetailWeatherViewModel::class.java)
        (activity as? AppCompatActivity)?.supportActionBar?.title = null

        bindUI()
    }

    private fun bindUI() = launch {
        val futureWeatherEntries = viewModel.detailWeather.await()
        val locationWeather = viewModel.weather.await()

        locationWeather.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            updateLocation(it.name)
        })

        futureWeatherEntries.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            updateToolbarSubtitle()
            updateTemperatures(
                it.temperature.toInt(),
                it.maxTemperature.toInt(),
                it.feelsLike.toInt(),
                it.minTemperature.toInt()
            )
            updatePressure(
                it.pressure
            )
            updateHumidity(
                it.humidity
            )
            it.entries.forEach { futureWeatherDays ->
                updateCondition(futureWeatherDays.description)
                updateWeatherIcon(imageView_condition_icon, futureWeatherDays.icon)
            }

        })
    }

    private fun updateToolbarSubtitle() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null
    }

    private fun updateCondition(description: String) {
        textView_condition.text = description
    }

    @SuppressLint("SetTextI18n")
    private fun updateHumidity(humidity: Int) {
        textView_humidity.text = "Humidity: $humidity %"
    }

    @SuppressLint("SetTextI18n")
    private fun updatePressure(pressure: Int) {
        textView_pressure.text = "Pressure: $pressure Pa"
    }

    @SuppressLint("SetTextI18n")
    private fun updateTemperatures(
        temp: Int,
        tempMax: Int,
        tempFeelsLike: Int,
        tempMin: Int
    ) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        if (unitAbbreviation == "°F") {
            val units = "°F"
            textView_temperature.text = "$temp$units"
            textView_min_max_temperature.text = "Min: $tempMin$units, Max: $tempMax$units"
            textView_feelsLike.text = "Feels like: $tempFeelsLike$units"
        } else if (unitAbbreviation == "°C") {
            val units = "°C"
            textView_temperature.text = "${tempToMetric(temp)}$units"
            textView_min_max_temperature.text =
                "Min: ${tempToMetric(tempMin)}$units, Max: ${tempToMetric(tempMax)}$units"
            textView_feelsLike.text = "Feels like: ${tempToMetric(tempFeelsLike)}$units"
        }
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isImperial) imperial else metric
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }
}
