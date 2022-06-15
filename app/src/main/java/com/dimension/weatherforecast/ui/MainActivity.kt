package com.dimension.weatherforecast.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dimension.weatherforecast.R
import com.dimension.weatherforecast.databinding.ActivityMainBinding
import com.dimension.weatherforecast.db.WeatherDataBase
import com.dimension.weatherforecast.repository.WeatherRepository
import com.dimension.weatherforecast.ui.fragments.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val weatherRepository = WeatherRepository(WeatherDataBase(this))
        val viewModelFactory = WeatherViewModelProviderFactory(weatherRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[WeatherViewModel::class.java]


        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, MainFragment())
            commit()
        }
    }
}