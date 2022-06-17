package com.dimension.weatherforecast.ui.fragments

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


class LocalWeather : Fragment() {
    private lateinit var viewModel: WeatherViewModel
    private var binding: WeatherFragmentBinding? = null
    lateinit var forecastAdapter: ForecastAdapter
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 99

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = WeatherFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()
        (activity as MainActivity?)?.supportActionBar?.title = "Local Weather"

        if (isLocationPermissionGranted()) {
            getWeatherByLocation()
        } else {
            // if location permission denied
            binding?.pbWeather?.visibility = View.GONE
            binding?.weatherHide?.visibility = View.GONE
            binding?.tvAlert?.visibility = View.VISIBLE
        }

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
                    // data is loading
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

        binding?.swipeRefreshLayout?.setOnRefreshListener {
            binding?.swipeRefreshLayout?.isRefreshing = false
            getWeatherByLocation()

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

    // returns date format string
    private fun getTime(s: Int): String? {
        try {
            val sdf = SimpleDateFormat("HH:mm")
            val netDate = Date(s.toLong() * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    // get data by location
    @SuppressLint("MissingPermission")
    fun getWeatherByLocation() {
        val locationTask =
            activity?.let { LocationServices.getFusedLocationProviderClient(it).lastLocation }

        locationTask?.addOnSuccessListener { location ->
            viewModel.getWeather(City("Local", location.latitude, location.longitude))
        }

        locationTask?.addOnFailureListener() {
            object : OnFailureListener {
                override fun onFailure(p0: java.lang.Exception) {
                    view?.let { it ->
                        Snackbar.make(it, "Please check location", Snackbar.LENGTH_LONG)
                            .setMarginAndShow()
                    }
                }
            }
        }

    }

    //check permission
    fun isLocationPermissionGranted(): Boolean {
        if (activity?.let {
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
                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                )
            }
            return false
        } else {
            return true
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] === PackageManager.PERMISSION_GRANTED
                ) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    view?.let {
                        Snackbar.make(it, "Permission granted", Snackbar.LENGTH_LONG
                        ).setMarginAndShow()
                    }
                    //show layout and get data
                    binding?.weatherHide?.visibility = View.VISIBLE
                    binding?.tvAlert?.visibility = View.GONE
                    getWeatherByLocation()
                } else {
                    // permission denied
                    view?.let {
                        Snackbar.make(it, "Permission denied", Snackbar.LENGTH_LONG)
                            .setMarginAndShow()
                    }
                }
                return
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}