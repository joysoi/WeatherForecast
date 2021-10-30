package com.example.weatherforecast.ui.weather.current

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import org.kodein.di.android.x.closestKodein
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.weatherforecast.R
import com.example.weatherforecast.data.adapter.HourlyWeatherItem
import com.example.weatherforecast.data.model.hourly.Hourly
import com.example.weatherforecast.data.model.hourly.OneCallResponse
import com.example.weatherforecast.internal.tempToMetric
import com.example.weatherforecast.internal.updateWeatherIcon
import com.example.weatherforecast.ui.base.ScopedFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.current_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class CurrentFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val currentViewModelFactory: CurrentViewModelFactory by instance()
    private lateinit var viewModel: CurrentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, currentViewModelFactory).get(CurrentViewModel::class.java)
        (activity as? AppCompatActivity)?.supportActionBar?.title = null

        bindUI()
    }

    private fun bindUI() = launch {
        val resultCurrentWeather = viewModel.weather.await()
        val resultHourlyWeather = viewModel.oneCallHourlyWeather.await()
        val notificationResult = viewModel.notification

        resultCurrentWeather.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            group_loading.visibility = View.GONE
            updateLocation(it.name)
            updateDateToToday()
            notificationResult.showNotificationForCurrentWeatherLocation(
                it.name,
                it.entries[0].description,
                updateNotificationTemperature(it.temperature.toInt())
            )
            updateTemperatures(
                it.temperature.toInt(),
                it.maxTemperature.toInt(),
                it.minTemperature.toInt()
            )
            it.entries.forEach {
                updateWeatherDescription(it.description)
                updateWeatherIcon(weatherIcon, it.icon)
            }
        })

        resultHourlyWeather.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            initRecyclerView(it.hourly.toHourlyWeatherItem(it))
        })

    }

    private fun List<Hourly>.toHourlyWeatherItem(oneCallResponse: OneCallResponse): List<HourlyWeatherItem> {
        return this.map {
            HourlyWeatherItem(it, oneCallResponse, viewModel)
        }
    }

    private fun initRecyclerView(items: List<HourlyWeatherItem>) {
        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(items)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(
                this@CurrentFragment.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = groupAdapter
        }
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToToday() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

    private fun updateWeatherDescription(desc: String) {
        descriptionTxt.text = desc
    }

    @SuppressLint("SetTextI18n")
    private fun updateTemperatures(temp: Int, tempMax: Int, tempMin: Int) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        if (unitAbbreviation == "°F") {
            val units = "°F"
            tempTxt.text = "$temp$units"
            tempMaxText.text = "Max:\n$tempMax$units"
            tempMinText.text = "Min:\n$tempMin$units"
        } else if (unitAbbreviation == "°C") {
            val units = "°C"
            tempTxt.text = "${tempToMetric(temp)}$units"
            tempMaxText.text = "Max:\n${tempToMetric(tempMax)}$units"
            tempMinText.text = "Min:\n${tempToMetric(tempMin)}$units"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateNotificationTemperature(temp: Int): Int {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        if (unitAbbreviation == "°F") {
            return temp
        } else if (unitAbbreviation == "°C") {
            return tempToMetric(temp)
        }
        return -1
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isImperial) imperial else metric
    }
}

