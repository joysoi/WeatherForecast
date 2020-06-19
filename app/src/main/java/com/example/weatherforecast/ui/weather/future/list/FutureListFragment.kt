package com.example.weatherforecast.ui.weather.future.list

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import org.kodein.di.android.x.closestKodein

import com.example.weatherforecast.R
import com.example.weatherforecast.data.adapter.FutureWeatherItem
import com.example.weatherforecast.data.entity.FutureWeatherItems
import com.example.weatherforecast.internal.stringArgsToLocalDateTime
import com.example.weatherforecast.ui.base.ScopedFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.future_list_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import org.threeten.bp.LocalDateTime

class FutureListFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val futureViewModelFactory: FutureViewModelFactory by instance()
    private lateinit var viewModel: FutureListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, futureViewModelFactory).get(FutureListViewModel::class.java)
        (activity as? AppCompatActivity)?.supportActionBar?.title = null
        bindUI()
    }

    private fun bindUI() = launch {
        val futureWeatherEntries = viewModel.weatherEntries.await()
        val locationWeather = viewModel.weather.await()

        locationWeather.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            group_loading.visibility = View.GONE
            updateLocation(it.name)
        })

        futureWeatherEntries.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            group_loading.visibility = View.GONE
            updateDateToNextWeek()
            initRecyclerView(it.toFutureWeatherItems())
        })
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToNextWeek() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = context?.getString(R.string.next_seven_days)
    }

    private fun List<FutureWeatherItems>.toFutureWeatherItems(): List<FutureWeatherItem> {
        return this.map {
            FutureWeatherItem(it, viewModel)
        }
    }

    private fun initRecyclerView(items: List<FutureWeatherItem>) {
        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(items)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(
                this@FutureListFragment.context)
            adapter = groupAdapter
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? FutureWeatherItem)?.let {
                val stringToDate = stringArgsToLocalDateTime(it.futureWeather.date)
                showWeatherDetail(stringToDate!!, view)
            }
        }
    }

    private fun showWeatherDetail(date: LocalDateTime, view: View) {
        val actionDetail = FutureListFragmentDirections.actionDetail(date)
        Navigation.findNavController(view).navigate(actionDetail)
    }
}