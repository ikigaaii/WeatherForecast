package com.dimension.weatherforecast.ui


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dimension.weatherforecast.R
import com.dimension.weatherforecast.databinding.ActivityMainBinding
import com.dimension.weatherforecast.db.WeatherDataBase
import com.dimension.weatherforecast.repository.WeatherRepository


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: WeatherViewModel


    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setup viewVodel
        val weatherRepository = WeatherRepository(WeatherDataBase(this))
        val viewModelFactory = WeatherViewModelProviderFactory(weatherRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[WeatherViewModel::class.java]

        //setup navController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.frame_layout) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)


    }


    fun hideBottomNavigationView() {
        binding.bottomNavigation.clearAnimation()
        binding.bottomNavigation.animate()
            .translationY(binding.bottomNavigation.height.toFloat()).duration = 300
        binding.bottomNavigation.visibility = View.GONE
    }

    fun showBottomNavigationView() {
        binding.bottomNavigation.clearAnimation()
        binding.bottomNavigation.animate().translationY(0f).duration = 300
        binding.bottomNavigation.visibility = View.VISIBLE
    }


}