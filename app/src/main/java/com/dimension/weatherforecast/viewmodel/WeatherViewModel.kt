package com.dimension.weatherforecast.ui


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimension.weatherforecast.models.etc.Forecast
import com.dimension.weatherforecast.repository.WeatherRepository
import com.dimension.weatherforecast.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response


class WeatherViewModel(
    val weatherRepository: WeatherRepository
): ViewModel() {
    val dailyForecast: MutableLiveData<Resource<Forecast>> = MutableLiveData()


    init {
        getForecast()
    }

    fun getForecast() = viewModelScope.launch {
        val response = weatherRepository.getForecast(42.871, 74.582)

        dailyForecast.postValue(handleRandomRecipeResponse(response))
    }

    private fun handleRandomRecipeResponse(response: Response<Forecast>) : Resource<Forecast> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Succes(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}