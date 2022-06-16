package com.dimension.weatherforecast.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.lifecycle.Observer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimension.weatherforecast.R
import com.dimension.weatherforecast.adapters.CitiesAdapter
import com.dimension.weatherforecast.ui.MainActivity
import com.dimension.weatherforecast.databinding.CitiesSearchFragmentBinding
import com.dimension.weatherforecast.models.City
import com.dimension.weatherforecast.ui.WeatherViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CitiesSearchFragment : Fragment() {
    private lateinit var viewModel: WeatherViewModel
    private var binding: CitiesSearchFragmentBinding? = null
    lateinit var citiesAdapter:  CitiesAdapter
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
        (activity as MainActivity?)?.supportActionBar?.title = "Search Cities"
        setupRecyclerView()

        citiesAdapter.setOnCityItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("city", it)
            }

            findNavController().navigate(
                R.id.action_citiesSearchFragment_to_cityFragment,
                bundle
            )
        }

        var job : Job?  = null
        binding?.etCities?.addTextChangedListener { editable->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                editable?.let {
                    if(editable.toString().isNotEmpty()){
                        viewModel.searchCities(editable.toString())}
                }
            }
        }

        viewModel.foundCities.observe(viewLifecycleOwner, Observer {  response ->
              if(response.isNotEmpty()){
                      citiesAdapter?.differ?.submitList(response)
              }
        })

        super.onViewCreated(view, savedInstanceState)
    }
    private fun setupRecyclerView(){
        citiesAdapter = CitiesAdapter()
        binding?.rvSearchCity?.apply {
            adapter = citiesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null

    }

}