package com.dimension.weatherforecast.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dimension.weatherforecast.ui.MainActivity
import com.dimension.weatherforecast.databinding.CitiesSearchFragmentBinding
import com.dimension.weatherforecast.ui.WeatherViewModel

class CitiesSearchFragment : Fragment() {
    private lateinit var viewModel: WeatherViewModel
    private var binding: CitiesSearchFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CitiesSearchFragmentBinding.inflate(inflater, container, false)
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