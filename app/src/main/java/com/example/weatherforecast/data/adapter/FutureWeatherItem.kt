package com.example.weatherforecast.data.adapter

import android.annotation.SuppressLint
import android.widget.TextView
import com.example.weatherforecast.R
import com.example.weatherforecast.data.entity.FutureWeatherItems
import com.example.weatherforecast.internal.tempToMetric
import com.example.weatherforecast.internal.updateWeatherIcon
import com.example.weatherforecast.ui.weather.future.list.FutureListViewModel
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_future_weather.*

class FutureWeatherItem(
    val futureWeather: FutureWeatherItems,
    private val viewModel: FutureListViewModel
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            futureWeather.entries.forEach {
                textView_condition.text = it.description
            }
            updateTemperature()
            updateConditionImage()
        }
    }

    override fun getLayout() = R.layout.item_future_weather


    private fun GroupieViewHolder.updateTemperature() {
        val futureTemp = futureWeather.temperature.toInt()
        tempAbbreviation(futureTemp, textView_temperature)
    }

    private fun GroupieViewHolder.updateConditionImage() {
        futureWeather.entries.forEach {
            val icon = it.icon
            updateWeatherIcon(imageView_condition_icon, icon)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun tempAbbreviation(temp: Int, tempView: TextView) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        if (unitAbbreviation == "°F") {
            val units = "°F"
            tempView.text = "$temp$units"
        } else if (unitAbbreviation == "°C") {
            val units = "°C"
            tempView.text = "${tempToMetric(temp)}$units"
        }
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isImperial) imperial else metric
    }

}