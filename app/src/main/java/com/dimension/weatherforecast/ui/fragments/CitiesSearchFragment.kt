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
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimension.weatherforecast.adapters.CitiesSearchAdapter
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
    lateinit var citiesAdapter:  CitiesSearchAdapter
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
        (activity as MainActivity?)?.supportActionBar?.hide()
        setupRecyclerView()
        citiesAdapter.setOnCityItemClickListener {
            val myDialogFragment = CityAddDialog(it, viewModel, parentFragmentManager)
            val manager = parentFragmentManager
            myDialogFragment.show(manager, "myDialog")

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
        citiesAdapter = CitiesSearchAdapter()
        binding?.rvSearchCity?.apply {
            adapter = citiesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity?)?.supportActionBar?.show()
        binding = null
    }

    class CityAddDialog(cit : City, vmodel : WeatherViewModel, fragmentManager: FragmentManager) : DialogFragment() {
        val city = cit
        val viewModel = vmodel
        val manager = fragmentManager

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setTitle("Save city?")
                    .setMessage(city.name)
                    .setCancelable(true)
                    .setPositiveButton("Save") { dialog, id ->
                        viewModel.saveCity(city)
                        Toast.makeText(activity, "City ${city.name} saved",
                            Toast.LENGTH_LONG).show()
                        manager.popBackStack()
                    }
                    .setNegativeButton("Cancel",
                        DialogInterface.OnClickListener { dialog, id ->

                        })
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }
}