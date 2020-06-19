package com.example.weatherforecast.data.adapter

import android.annotation.SuppressLint
import android.widget.TextView
import com.example.weatherforecast.R
import com.example.weatherforecast.data.model.hourly.Hourly
import com.example.weatherforecast.data.model.hourly.OneCallResponse
import com.example.weatherforecast.internal.tempToMetric
import com.example.weatherforecast.internal.updateWeatherIcon
import com.example.weatherforecast.ui.weather.current.CurrentViewModel
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_hourly.*
import java.text.SimpleDateFormat
import java.util.*

class HourlyWeatherItem(
    private val hourly: Hourly,
    private val oneCallResponse: OneCallResponse,
    private val viewModel: CurrentViewModel
) : Item(){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            updateHour()
            updateTemp()
            updateConditionImage()
        }
    }

    override fun getLayout(): Int = R.layout.item_hourly

    private fun GroupieViewHolder.updateHour(){
        val unixSeconds = hourly.dt
        hourTxt.text = parseToHour(unixSeconds)
    }



    private fun GroupieViewHolder.updateConditionImage() {
        hourly.weather.forEach {
            val icon = it.icon
            updateWeatherIcon(imageViewHourly, icon)
        }
    }

    private fun parseToHour(unixSeconds: Int): String{
        val date = Date(unixSeconds*1000L)
        val timeZone = oneCallResponse.timezone
        val sdf = SimpleDateFormat("h a", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone(timeZone)
        return sdf.format(date)
    }

    private fun GroupieViewHolder.updateTemp(){
        val tempHourly = hourly.temp.toInt()
        tempAbbreviation(tempHourly, tempHourTxt)
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