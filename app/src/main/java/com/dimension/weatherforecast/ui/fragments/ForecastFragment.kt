package com.dimension.weatherforecast.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dimension.weatherforecast.ui.MainActivity
import com.dimension.weatherforecast.databinding.ForecastFragmentBinding
import com.dimension.weatherforecast.ui.WeatherViewModel

class ForecastFragment : Fragment() {
    private lateinit var viewModel: WeatherViewModel
    private var binding: ForecastFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ForecastFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = (activity as MainActivity).viewModel
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}