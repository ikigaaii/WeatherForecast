package com.dimension.weatherforecast.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimension.weatherforecast.adapters.ForecastAdapter
import com.dimension.weatherforecast.databinding.MainFragmentBinding
import com.dimension.weatherforecast.ui.MainActivity
import com.dimension.weatherforecast.ui.WeatherViewModel
import com.dimension.weatherforecast.util.Resource
import java.text.SimpleDateFormat
import java.util.*


class MainFragment : Fragment() {
    private lateinit var viewModel: WeatherViewModel
    private var binding: MainFragmentBinding? = null
  //  lateinit var forecastAdapter:  ForecastAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = (activity as MainActivity).viewModel

     //   viewModel.dailyForecast.observe(viewLifecycleOwner, Observer { response ->
//
     //       when (response) {
     //           is Resource.Succes -> {
     //               response.data?.let {
     //                   if(it.data.isNotEmpty()){
     //                       val forecast = it.data[0]
     //                       binding?.rainChance?.text = "${forecast.pop}%"
     //                       binding?.sunriseTime?.text = "Sunrise " + getTime(forecast.sunrise_ts)
     //                       binding?.sunsetTime?.text = "Sunset " +  getTime(forecast.sunset_ts)
     //                       binding?.moonriseTime?.text = "Moonrise " +  getTime(forecast.moonrise_ts)
     //                       binding?.moonsetTime?.text = "Moonset " +  getTime(forecast.moonset_ts)
     //                       binding?.pressureText?.text = "${forecast.pres.toInt()}mbar"
     //                       binding?.visibilityText?.text = "${forecast.vis}km"
     //                       binding?.cloudsText?.text = "${forecast.clouds}%"
     //                       binding?.aqIndexText?.text = forecast.aqi.toString()
//
     //                       binding?.dewPointText?.text = "${forecast.dewpt}°C"
//
     //                   //    forecastAdapter?.differ?.submitList(it.data)
     //                   }
     //               }
     //           }
     //           is Resource.Error -> {
     //               response.message?.let { message ->
     //                   Log.e("Main Fragment ", "Error $message")
     //               }
     //           }
     //       }
     //   })

        viewModel.currentWeather.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Succes -> {
                    response.data?.let {
                        if(it.data.isNotEmpty()){
                            val current = it.data[0]
                            (activity as MainActivity?)?.supportActionBar?.title = current.city_name
                            binding?.weatherDescrpt?.text = current.weather.description
                            binding?.weatherTemp?.text = "${current.temp.toInt()}°C"
                            binding?.weatherFeels?.text = "Feels like " +  current.app_temp
                            binding?.windSpeed?.text ="${current.wind_spd}m/s"
                            binding?.humidityText?.text = "${current.rh}%"
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

        super.onViewCreated(view, savedInstanceState)
    }

  //  private fun setupRecyclerView(){
  //      forecastAdapter = ForecastAdapter()
  //      binding?.rvForecast?.apply {
  //          adapter = forecastAdapter
  //          layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
  //      }
  //  }

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
}