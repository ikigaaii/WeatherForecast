package com.dimension.weatherforecast.ui.fragments

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimension.weatherforecast.adapters.ForecastAdapter
import com.dimension.weatherforecast.databinding.WeatherFragmentBinding
import com.dimension.weatherforecast.models.City
import com.dimension.weatherforecast.ui.MainActivity
import com.dimension.weatherforecast.ui.WeatherViewModel
import com.dimension.weatherforecast.util.Resource
import com.dimension.weatherforecast.util.setMarginAndShow
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


class MainFragment : Fragment() {
    private lateinit var viewModel: WeatherViewModel
    private var binding: WeatherFragmentBinding? = null
    lateinit var forecastAdapter: ForecastAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = WeatherFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(savedInstanceState == null) {
            getWeatherByLocation()
            viewModel = (activity as MainActivity).viewModel

            setupRecyclerView()
            (activity as MainActivity?)?.supportActionBar?.title = "Local Weather"

            viewModel.dailyForecast.observe(viewLifecycleOwner, Observer { response ->
                when (response) {
                    is Resource.Succes -> {
                        response.data?.let {
                            if (it.data.isNotEmpty()) {
                                val forecast = it.data[0]
                                binding?.rainChance?.text = "${forecast.pop}%"
                                binding?.sunriseTime?.text =
                                    "Sunrise " + getTime(forecast.sunrise_ts)
                                binding?.sunsetTime?.text = "Sunset " + getTime(forecast.sunset_ts)
                                binding?.moonriseTime?.text =
                                    "Moonrise " + getTime(forecast.moonrise_ts)
                                binding?.moonsetTime?.text =
                                    "Moonset " + getTime(forecast.moonset_ts)
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
                            Log.e("Main Fragment ", "Error $message")
                        }
                    }
                }
            })

            viewModel.currentWeather.observe(viewLifecycleOwner, Observer { response ->
                when (response) {
                    is Resource.Succes -> {
                        response.data?.let {
                            binding?.pbWeather?.visibility = View.GONE
                            binding?.weatherHide?.visibility = View.VISIBLE
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
                    is Resource.Loading -> {
                        binding?.pbWeather?.visibility = View.VISIBLE
                        binding?.weatherHide?.visibility = View.INVISIBLE
                    }
                    is Resource.Error -> {
                        response.message?.let { message ->
                            Log.e("Main Fragment ", "Error $message")
                        }
                    }
                }
            })
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
    }


    @SuppressLint("MissingPermission")
    fun getWeatherByLocation() {
        if (isLocationPermissionGranted()) {
            val locationTask =
                activity?.let { LocationServices.getFusedLocationProviderClient(it).lastLocation }
            locationTask?.addOnSuccessListener { location ->

                viewModel.getWeather(City("Local", location.latitude, location.longitude))
            }
            locationTask?.addOnFailureListener() {
                object : OnFailureListener {
                    override fun onFailure(p0: java.lang.Exception) {
                        view?.let { it ->
                            Snackbar.make(
                                it,
                                "Please check location",
                                Snackbar.LENGTH_LONG
                            ).setMarginAndShow()
                        }
                    }
                }
            }

        } else view?.let { it ->
            Snackbar.make(it, "Please check location", Snackbar.LENGTH_LONG).setMarginAndShow()
        }
    }


    fun isLocationPermissionGranted(): Boolean {
        return if (activity?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && activity?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    10
                )
            }
            false
        } else {
            true
        }
    }

}