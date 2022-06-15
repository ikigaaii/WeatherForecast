package com.dimension.weatherforecast.ui


import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimension.weatherforecast.models.df.Current
import com.dimension.weatherforecast.models.etc.Forecast
import com.dimension.weatherforecast.repository.WeatherRepository
import com.dimension.weatherforecast.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response


class WeatherViewModel(
    val weatherRepository: WeatherRepository
): ViewModel() {
    val dailyForecast: MutableLiveData<Resource<Forecast>> = MutableLiveData()
    val currentWeather: MutableLiveData<Resource<Current>> = MutableLiveData()


    init {
        getCurrent()
        getForecast()
    }

    fun getForecast() = viewModelScope.launch {
        val response = weatherRepository.getForecast(42.871, 74.582)

        dailyForecast.postValue(handleForecast(response))
    }

    private fun handleForecast(response: Response<Forecast>) : Resource<Forecast> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Succes(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getCurrent() = viewModelScope.launch {
        val response = weatherRepository.getCurrent(42.871, 74.582)
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

}