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
    //local weather
    val dailyForecast: MutableLiveData<Resource<Forecast>> = MutableLiveData()
    val currentWeather: MutableLiveData<Resource<Current>> = MutableLiveData()

    //particular city weather
    val chosenDailyForecast: MutableLiveData<Resource<Forecast>> = MutableLiveData()
    val chosenCurrentWeather: MutableLiveData<Resource<Current>> = MutableLiveData()

    //city data
    val foundCities: MutableLiveData<CityList> = MutableLiveData()
    val savedCities: MutableLiveData<List<City>> = MutableLiveData()
    val savedCitiesNameList: MutableLiveData<List<String>> = MutableLiveData()



    fun getWeather(city: City) = viewModelScope.launch {
        currentWeather.postValue(Resource.Loading())
        val forecastResponse = weatherRepository.getForecast(city.latitude, city.longitude)
        dailyForecast.postValue(handleForecast(forecastResponse))
        val currentResponse = weatherRepository.getCurrent(city.latitude, city.longitude)
        currentWeather.postValue(handleCurrent(currentResponse))
    }

    fun getChosenWeather(city: City) = viewModelScope.launch {
        chosenCurrentWeather.postValue(Resource.Loading())
        val forecastResponse = weatherRepository.getForecast(city.latitude, city.longitude)
        chosenDailyForecast.postValue(handleForecast(forecastResponse))
        val currentResponse = weatherRepository.getCurrent(city.latitude, city.longitude)
        chosenCurrentWeather.postValue(handleCurrent(currentResponse))
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
        getSavedCitiesName()
    }

    fun getSavedCities() = viewModelScope.launch {
        savedCities.postValue(weatherRepository.getAllCities())
    }

    fun getSavedCitiesName() = viewModelScope.launch {
        savedCitiesNameList.postValue(weatherRepository.getCitiesName())
    }

    fun deleteCity(city: City) = viewModelScope.launch {
        weatherRepository.deleteCity(city)
        getSavedCities()
        getSavedCitiesName()
    }

    private fun handleForecast(response: Response<Forecast>) : Resource<Forecast> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Succes(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    private fun handleCurrent(response: Response<Current>) : Resource<Current> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Succes(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }




}