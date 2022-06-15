package com.dimension.weatherforecast.ui


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimension.weatherforecast.models.City
import com.dimension.weatherforecast.models.CityList
import com.dimension.weatherforecast.models.Current
import com.dimension.weatherforecast.models.Forecast
import com.dimension.weatherforecast.repository.WeatherRepository
import com.dimension.weatherforecast.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response


class WeatherViewModel(
    val weatherRepository: WeatherRepository
): ViewModel() {
    val dailyForecast: MutableLiveData<Resource<Forecast>> = MutableLiveData()
    val currentWeather: MutableLiveData<Resource<Current>> = MutableLiveData()
    val foundCities: MutableLiveData<CityList> = MutableLiveData()
    val savedCities: MutableLiveData<List<City>> = MutableLiveData()


    init {
        getCurrent(City(99, "City", 42.871, 74.582))
        getForecast(City(99, "City", 42.871, 74.582))
        getSavedCities()
    }

    fun getForecast(city: City) = viewModelScope.launch {
        val response = weatherRepository.getForecast(city.latitude, city.longitude)
        dailyForecast.postValue(handleForecast(response))
    }
    fun getByCity(city: City){
        getCurrent(city)
        getForecast(city)
    }

    private fun handleForecast(response: Response<Forecast>) : Resource<Forecast> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Succes(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getCurrent(city: City) = viewModelScope.launch {
        val response = weatherRepository.getCurrent(city.latitude, city.longitude)
        currentWeather.postValue(handleCurrent(response))
    }

    private fun handleCurrent(response: Response<Current>) : Resource<Current> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Succes(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun searchCities(str: String) = viewModelScope.launch {
        val response = weatherRepository.searchCities(str)
        response.isSuccessful.let {
            foundCities.postValue(response.body())
        }
    }

    fun saveCity(city: City) = viewModelScope.launch {
        weatherRepository.upsert(city)
        getSavedCities()
    }


    fun getSavedCities() = viewModelScope.launch {
        savedCities.postValue(weatherRepository.getAllCities())
    }


    fun deleteCity(city: City) = viewModelScope.launch {
        weatherRepository.deleteCity(city)
        getSavedCities()
    }




}