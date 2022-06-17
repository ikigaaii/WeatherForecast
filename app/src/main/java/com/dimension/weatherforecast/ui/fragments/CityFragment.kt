package com.dimension.weatherforecast.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimension.weatherforecast.R
import com.dimension.weatherforecast.adapters.ForecastAdapter
import com.dimension.weatherforecast.databinding.CityFragmentBinding
import com.dimension.weatherforecast.ui.MainActivity
import com.dimension.weatherforecast.ui.WeatherViewModel
import com.dimension.weatherforecast.util.Resource
import com.dimension.weatherforecast.util.setMarginAndShow
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


class CityFragment : Fragment() {
    private lateinit var viewModel: WeatherViewModel
    private var binding: CityFragmentBinding? = null
    val args: CityFragmentArgs by navArgs()
    lateinit var forecastAdapter: ForecastAdapter
    private var citiesNameList = listOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CityFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = (activity as MainActivity).viewModel
        viewModel.getSavedCitiesName()
        setupRecyclerView()

        //get particular city data
        viewModel.getChosenWeather(args.city)



        viewModel.chosenDailyForecast.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Resource.Succes -> {
                    response.data?.let {
                        if (it.data.isNotEmpty()) {
                            val forecast = it.data[0]
                            binding?.rainChance?.text = "${forecast.pop}%"
                            binding?.sunriseTime?.text = "Sunrise " + getTime(forecast.sunrise_ts)
                            binding?.sunsetTime?.text = "Sunset " + getTime(forecast.sunset_ts)
                            binding?.moonriseTime?.text =
                                "Moonrise " + getTime(forecast.moonrise_ts)
                            binding?.moonsetTime?.text = "Moonset " + getTime(forecast.moonset_ts)
                            binding?.pressureText?.text = "${forecast.pres.toInt()}mbar"
                            binding?.visibilityText?.text = "${forecast.vis}km"
                            binding?.cloudsText?.text = "${forecast.clouds}%"
                            binding?.aqIndexText?.text = forecast.aqi.toString()

                            binding?.dewPointText?.text = "${forecast.dewpt}°C"

                            forecastAdapter.differ.submitList(it.data)
                            forecastAdapter.notifyDataSetChanged()
                        }
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e("City Fragment ", "Error $message")
                    }
                }
            }
        })

        viewModel.chosenCurrentWeather.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Succes -> {
                    response.data?.let {
                        binding?.pbWeather?.visibility = View.GONE
                        binding?.statsHide?.visibility = View.VISIBLE
                        if (it.data.isNotEmpty()) {
                            val current = it.data[0]
                            binding?.tvCity?.text = current.city_name
                            binding?.weatherDescrpt?.text = current.weather.description
                            binding?.weatherTemp?.text = "${current.temp.toInt()}°C"
                            binding?.weatherFeels?.text = "Feels like " + current.app_temp
                            binding?.windSpeed?.text = "${current.wind_spd}m/s"
                            binding?.humidityText?.text = "${current.rh.toInt()}%"
                        }
                    }
                }
                // data loading...
                is Resource.Loading -> {
                    binding?.pbWeather?.visibility = View.VISIBLE
                    binding?.statsHide?.visibility = View.INVISIBLE
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e("City Fragment ", "Error $message")
                    }
                }
            }
        })

        viewModel.savedCitiesNameList.observe(viewLifecycleOwner, Observer {
            citiesNameList = it
            if (!citiesNameList.contains(args.city.name)) {
                binding?.ivFav?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_unselected
                    )
                )
            }
        })

        //saving and deleting city
        binding?.ivFav?.setOnClickListener {

            if (citiesNameList.contains(args.city.name)) {
                viewModel.deleteCity(args.city)
                Snackbar.make(view, "City deleted", Snackbar.LENGTH_SHORT).setMarginAndShow()
                binding?.ivFav?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_unselected
                    )
                )
            } else {
                viewModel.saveCity(args.city)
                Snackbar.make(view, "City saved", Snackbar.LENGTH_SHORT).setMarginAndShow()
                binding?.ivFav?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_selected
                    )
                )
            }
        }
        //refresh data
        binding?.swipeRefreshLayout?.setOnRefreshListener {
            binding?.swipeRefreshLayout?.isRefreshing = false
            viewModel.getChosenWeather(args.city)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupRecyclerView() {
        forecastAdapter = ForecastAdapter()
        binding?.rvForecast?.apply {
            adapter = forecastAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    // returns time format string
    private fun getTime(s: Int): String? {
        try {
            val sdf = SimpleDateFormat("HH:mm")
            val netDate = Date(s.toLong() * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.showBottomNavigationView()
        }
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.hideBottomNavigationView()
        }
    }
}