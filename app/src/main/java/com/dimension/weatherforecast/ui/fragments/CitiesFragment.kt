package com.dimension.weatherforecast.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.dimension.weatherforecast.R
import com.dimension.weatherforecast.adapters.CitiesSearchAdapter
import com.dimension.weatherforecast.adapters.SavedCitiesAdapter
import com.dimension.weatherforecast.ui.MainActivity
import com.dimension.weatherforecast.databinding.CitiesFragmentBinding
import com.dimension.weatherforecast.models.City
import com.dimension.weatherforecast.ui.WeatherViewModel

class CitiesFragment : Fragment() {
    private lateinit var viewModel: WeatherViewModel
    private var binding: CitiesFragmentBinding? = null
    lateinit var citiesAdapter: SavedCitiesAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = CitiesFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = (activity as MainActivity).viewModel
        viewModel.getSavedCities()
        setupRecyclerView()
        (activity as MainActivity?)?.supportActionBar?.title = "Saved Cities"
        citiesAdapter.setOnCityItemClickListener {
            val myDialogFragment = CityDeleteDialog(it, viewModel)
            val manager = parentFragmentManager
            myDialogFragment.show(manager, "MyDialog")

        }

        viewModel.savedCities.observe(viewLifecycleOwner, Observer {  response ->
            if(response.isNotEmpty()){
                citiesAdapter?.differ?.submitList(response)
            }
        })
        super.onViewCreated(view, savedInstanceState)

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.city_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add ->{
                parentFragmentManager
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.flFragment, CitiesSearchFragment())
                    .commit()
                return true
            }


            else -> return super.onOptionsItemSelected(item)

        }

    }

    private fun setupRecyclerView(){
        citiesAdapter = SavedCitiesAdapter()
        binding?.rvSavedCities?.apply {
            adapter = citiesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    class CityDeleteDialog(cit : City, vmodel : WeatherViewModel) : DialogFragment() {
        val city = cit
        val viewModel = vmodel

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setTitle("Delete city?")
                    .setMessage(city.name)
                    .setCancelable(true)
                    .setPositiveButton("Delete") { dialog, id ->
                        viewModel.deleteCity(city)
                        Toast.makeText(activity, "City ${city.name} deleted",
                            Toast.LENGTH_LONG).show()
                    }
                    .setNegativeButton("Cancel",
                        DialogInterface.OnClickListener { dialog, id ->

                        })
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }
}