package com.dimension.weatherforecast.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimension.weatherforecast.R
import com.dimension.weatherforecast.adapters.CitiesAdapter
import com.dimension.weatherforecast.databinding.CitiesListFragmentBinding
import com.dimension.weatherforecast.ui.MainActivity
import com.dimension.weatherforecast.ui.WeatherViewModel

class CitiesListFragment : Fragment() {
    private lateinit var viewModel: WeatherViewModel
    private var binding: CitiesListFragmentBinding? = null
    lateinit var citiesAdapter: CitiesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = CitiesListFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()
        (activity as MainActivity?)?.supportActionBar?.title = "Saved Cities"
        viewModel.getSavedCities()

        // show city data
        citiesAdapter.setOnCityItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("city", it)
            }
            findNavController().navigate(
                R.id.action_citiesFragment_to_cityFragment,
                bundle
            )
        }

        //update recyclerView
        viewModel.savedCities.observe(viewLifecycleOwner, Observer { response ->
            if (response.isNotEmpty()) {
                citiesAdapter?.differ?.submitList(response)
            }
        })
        super.onViewCreated(view, savedInstanceState)

    }


    private fun setupRecyclerView() {
        citiesAdapter = CitiesAdapter()
        binding?.rvSavedCities?.apply {
            adapter = citiesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}