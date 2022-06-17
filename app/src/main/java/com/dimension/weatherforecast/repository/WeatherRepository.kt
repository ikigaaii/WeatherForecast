package com.dimension.weatherforecast.repository


import com.dimension.tasty.api.RetrofitInstance
import com.dimension.weatherforecast.db.WeatherDataBase
import com.dimension.weatherforecast.models.City
import com.dimension.weatherforecast.util.Constants.Companion.WEATHER_API_KEY


class WeatherRepository(
    val db: WeatherDataBase
) {
    suspend fun getForecast(latitude: Double, longitude: Double) =
        RetrofitInstance.api.getDailyForecast(latitude,  longitude, WEATHER_API_KEY)


    suspend fun getCurrent(latitude: Double, longitude: Double) =
        RetrofitInstance.api.getCurrentWeather(latitude,  longitude, WEATHER_API_KEY)

    // city search api
    suspend fun searchCities(str: String) =
        RetrofitInstance.api.searchCities("https://api.api-ninjas.com/v1/city?limit=50&name=" + str)

    //update-insert
    suspend fun upsert(city: City) = db.getWeatherDao().upsert(city)

    suspend fun getAllCities() = db.getWeatherDao().getAllCities()

    suspend fun getCitiesName() = db.getWeatherDao().getCitiesName()

    suspend fun deleteCity(city: City) = db.getWeatherDao().deleteCity(city)

}

