package com.dimension.weatherforecast.ui.fragments

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.dimension.weatherforecast.ui.MainActivity
import com.dimension.weatherforecast.databinding.MainFragmentBinding
import com.dimension.weatherforecast.ui.WeatherViewModel
import com.dimension.weatherforecast.util.Resource
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream


class MainFragment : Fragment() {
    private lateinit var viewModel: WeatherViewModel
    private var binding: MainFragmentBinding? = null

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

        viewModel.dailyForecast.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Resource.Succes -> {
                    response.data?.let {
                        val forecast = it.dailyList[0]
                        binding?.weatherDescrpt?.text = forecast.weather.description
                        binding?.weatherFeels?.text = forecast.high_temp.toString()

                        binding?.sunriseTime?.text = forecast.sunrise_ts.toString()
                        binding?.sunsetTime?.text = forecast.sunset_ts.toString()
                        binding?.moonriseTime?.text = forecast.moonrise_ts.toString()
                        binding?.moonsetTime?.text = forecast.moonset_ts.toString()

                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e("Random Recipe ", "Error $message")
                    }
                }
            }


        })





        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}